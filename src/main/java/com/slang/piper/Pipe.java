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

    public Pipe() {
    }

    public Pipe(ThreadType threadType) {
        mInputThreadType = threadType;
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
            mNextPipe.input(mPendingData);
            mPendingData = null;
        }

        if(mPendingComplete) {
            mNextPipe.complete();
            mPendingComplete = false;
        }

        return rhs;
    }

    public void input(final I input) {
        if(mInputThreadType == ThreadType.ThreadDefault) {
            handleInput(input);
        } else {
            executeOnThread(new Runnable() { public void run() {
                handleInput(input);
            } } );
        }
    }

    public void complete() {
        if(mInputThreadType == ThreadType.ThreadDefault) {
            handleInputComplete();
        } else {
            executeOnThread(new Runnable() {
                public void run() {
                    handleInputComplete();
                }
            });
        }
    }

    /*------------------------------------------------------------------------------------------
     * Protected Methods
     ------------------------------------------------------------------------------------------*/

    protected synchronized void passOutput(O output) {
        if(mNextPipe == null) {
            mPendingData = output;
        } else {
            mNextPipe.input(output);
        }
    }

    protected synchronized void passComplete() {
        if(mNextPipe == null) {
            mPendingComplete = true;
        } else {
            mNextPipe.complete();
        }
    }

    /**
     * Called from previous pipe, passes it's output into here
     * @param mInput       Previous pipes output, this pipes input
     */
    protected void handleInput(I mInput) { }

    /**
     * Called from previous pipe when it's finished outputting
     */
    protected void handleInputComplete() { }


    /**
     * Executes operation on thread specified by next pipe
     * @param op        Runnable/operation to execute
     */
    protected void executeOnThread(Runnable op) {
        if(mInputThreadType == ThreadType.ThreadMain) {
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

    /**
     * @return
     */
    protected Handler getMainHandler() {
        if(_mainHandler == null) {
            _mainHandler = new Handler(Looper.getMainLooper());
        }
        return _mainHandler;
    }

    private static Handler _mainHandler;
}
