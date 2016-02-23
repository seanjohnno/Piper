package com.slang.piper;

/**
 * Represents a single operation pipe; input > transform > output
 * @param <I>   Input type
 * @param <O>   Output type
 */
public abstract class SinglePipe<I, O> extends Pipe<I, O> {

    public SinglePipe() {
    }

    /**
     * Accepts input, calls process, passes output to next pipe
     * @param input
     */
    @Override
    public void handleInput(I input) {
        O output = process(input);
        passOutput(output);
        passComplete();
    }

    protected abstract O process(I input);
}
