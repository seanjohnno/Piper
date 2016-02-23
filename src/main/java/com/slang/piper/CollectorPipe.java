package com.slang.piper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a pipe which accepts lots of input items and outputs a list
 * @param <I>   Input (I) and outputs List<I>
 */
public class CollectorPipe<I> extends Pipe<I, Collection<I>> {

    /**
     * List of input items
     */
    private List<I> mInputItems;

    /**
     * Constructor
     */
    public CollectorPipe() {
    }

    /**
     * Adds our input items to the list
     * @param input
     */
    @Override
    public void handleInput(I input) {
        getList().add(input);
    }

    /**
     * Called by previous pipe, pass all items as List<I> to next pipe
     */
    @Override
    public void handleInputComplete() {
        passOutput(getList());
        passComplete();
    }

    /**
     * @return      The list implementation we're using
     */
    protected List<I> getList() {
        if(mInputItems == null) {
            mInputItems = createList();
        }
        return mInputItems;
    }

    /**
     * @return      Creates the list implementation we're using, default returns ArrayList
     */
    protected List<I> createList() {
        return new ArrayList<>();
    }
}
