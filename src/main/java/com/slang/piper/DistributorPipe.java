package com.slang.piper;

import java.util.ArrayList;
import java.util.List;

/**
 * Pipe which can have multiple connected pipes, passes input onto each of them
 * @param <O>
 */
public class DistributorPipe<O> extends Pipe<O, O> {

    private List<Pipe<O, ?>> mPipeList = new ArrayList<>();

    public DistributorPipe() {

    }

    public DistributorPipe(ThreadType type) {
        super(type);
    }

    @Override
    public synchronized <N> Pipe<O, N> connect(Pipe<O, N> rhs) {
        if(mPipeList == null) {
            mPipeList = createPipeList();
        }
        mPipeList.add(rhs);
        return rhs;
    }

    public List<Pipe<O, ?>> createPipeList() {
        return new ArrayList<>();
    }

    @Override
    public void handleInput(O input) {
        for (Pipe<O, ?> pipe : mPipeList) {
            pipe.input(input);
        }
    }
    public void handleInputComplete() {
        for (Pipe<O, ?> pipe : mPipeList) {
            pipe.complete();
        }
    }
}
