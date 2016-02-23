package com.slang.piper;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Base class for Pipe. Accepts an input (I) and passes an output (0) to the next pipe
 * @param <I>
 * @param <O>
 */
public abstract class Pipe<I, O> {

    /*------------------------------------------------------------------------------------------
     * Constants
     ------------------------------------------------------------------------------------------*/

    public enum ThreadType { ThreadIO, ThreadMain, ThreadDefault }

    /*------------------------------------------------------------------------------------------
     * Member Data
     ------------------------------------------------------------------------------------------*/

    private ThreadType mInputThreadType = ThreadType.ThreadDefault;

    private Pipe<O, ?> mNextPipe;

    private O mPendingData;

    private boolean mPendingComplete;

    /*------------------------------------------------------------------------------------------
     * Construction + Public Methods
     ------------------------------------------------------------------------------------------*/

    protected Pipe() {
    }

    /**
     * Connects pipe to this pipe (so when passOutput is called, it's output is passed into the
     * handleInput of the next/rhs pipe)
     * @param rhs   Pipe to connect
     * @param <N>   Output of next pipe (used to chain method calls/connections)
     * @return      Rhs pipe
     */
    public synchronized <N> Pipe<O, N> connect(Pipe<O, N> rhs) {
        mNextPipe = rhs;

        if(mPendingData != null) {
            O data = mPendingData;
            mPendingData = null;
            passOutput(data);
        }

        if(mPendingComplete == true) {
            mPendingComplete = false;
            passComplete();
        }

        return rhs;
    }

    /**
     * Called from previous pipe, passes it's output into here
     * @param mInput       Previous pipes output, this pipes input
     */
    public abstract void handleInput(I mInput);

    /**
     * Called from previous pipe when it's finished outputting
     */
    public void handleInputComplete() { }

    /**
     * Used to tell previous pipe what thread (main/io/current) to call handleInput / handleInputComplete on
     * @param threadType
     * @return  This pipe so method calls / connecting can be chained
     */
    public Pipe<I, O> handleInputOn(ThreadType threadType) {
        mInputThreadType = threadType;
        return this;
    }

    /**
     * @return      What thread (main/io/current) to call this pipes handleInput / handleInputComplete on
     */
    public ThreadType getHandleInputOn() {
        return mInputThreadType == null ? mInputThreadType : mInputThreadType;
    }

    /*------------------------------------------------------------------------------------------
     * Protected
     ------------------------------------------------------------------------------------------*/

    /**
     * Call to pass output onto the next pipe, calls passOutputOnThread
     * @param output    Output from thuis pipe that serves as input to the next
     */
    protected synchronized void passOutput(O output) {
        if(mNextPipe != null) {
            passOutputOnThread(output);
        } else {
            mPendingData = output;
        }
    }

    /**
     * Calls handleInput() on next pipe on the thread it's specified
     * @param output
     */
    protected void passOutputOnThread(final O output) {
        Runnable op = new Runnable() {
            @Override
            public void run() {
                mNextPipe.handleInput(output);
            }
        };
        executeOnThread(op);
    }

    /**
     * If next pipe has been connected then this calls passCompleteOnThread. If it hasn't been connected
     * yet then it'll get the message immediately on a connect
     */
    protected synchronized void passComplete() {
        if(mNextPipe != null) {
            passCompleteOnThread();
        } else {
            mPendingComplete = true;
        }
    }

    /**
     * Calls handleInputComplete on connected pipe on thread specified by next pipe
     */
    protected void passCompleteOnThread() {
        Runnable op = new Runnable() {
            @Override
            public void run() {
                mNextPipe.handleInputComplete();
            }
        };
        executeOnThread(op);
    }

    /**
     * Executes operation on thread specified by next pipe
     * @param op        Runnable/operation to execute
     */
    protected void executeOnThread(Runnable op) {
        if(mNextPipe.mInputThreadType == ThreadType.ThreadDefault) {
            op.run();
        } else if(mNextPipe.mInputThreadType == ThreadType.ThreadMain) {
            getMainHandler().post(op);
        } else {
            getIOExecutor().execute(op);
        }
    }

    /**
     * @return      Uses THREAD_POOL_EXECUTOR, can be overriden to provide your own executor
     */
    protected Executor getIOExecutor() {
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }

    /*------------------------------------------------------------------------------------------
     * Static handle to main thread Handler
     ------------------------------------------------------------------------------------------*/

    private static Handler _mainHandler;

    private static Handler getMainHandler() {
        if(_mainHandler == null) {
            _mainHandler = new Handler(Looper.getMainLooper());
        }
        return _mainHandler;
    }
}
