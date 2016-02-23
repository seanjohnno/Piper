package com.slang.piper;

import java.util.Collection;

/**
 * Represents a pipe which takes a collection, splits the items and passes each separately onto
 * next pipe
 * @param <I>   Collection<I> is input, I as output
 */
public class CollectionSplitterPipe<I> extends Pipe<Collection<I>, I> {

    /**
     * Construction
     */
    public CollectionSplitterPipe() {
    }

    /**
     * Collection is input, passes each item individually onto next pipe then calls complete
     * @param input
     */
    @Override
    public void handleInput(Collection<I> input) {
        if(input != null) {
            for(I item : input) {
                passOutput(item);
            }
        }
        passComplete();
    }
}
