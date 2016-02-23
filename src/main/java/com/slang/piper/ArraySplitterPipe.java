package com.slang.piper;

/**
 * Represents a pipe which takes an array, splits the items and passes each separately onto
 * next pipe
 * @param <I>   Collection<I> is input, I as output
 */
public class ArraySplitterPipe<I> extends Pipe<I[], I> {

    /**
     * Construction
     */
    public ArraySplitterPipe() {

    }

    /**
     * Collection is input, passes each item individually onto next pipe then calls complete
     * @param input
     */
    @Override
    public void handleInput(I[] input) {
        if(input != null) {
            for(I item : input) {
                passOutput(item);
            }
        }
        passComplete();
    }
}
