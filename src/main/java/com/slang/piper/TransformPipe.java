package com.slang.piper;

/**
 * Similar to SinglePipe but it only passes it's calls handleInputComplete() on next pipe when
 * previous pipe calls it on this
 */
public abstract class TransformPipe<I, O> extends Pipe<I, O> {

    public TransformPipe() {
    }

    public TransformPipe(ThreadType type) {
        super(type);
    }

    /**
     * Accepts input, calls process, passes output to next pipe
     * @param input
     */
    @Override
    public void handleInput(I input) {
        O output = process(input);
        passOutput(output);
    }

    @Override
    protected void handleInputComplete() {
        passComplete();
    }

    protected abstract O process(I input);
}
