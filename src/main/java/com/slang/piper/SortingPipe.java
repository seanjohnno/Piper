package com.slang.piper;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Takes a bunch of items and using the comparator passed into the constructor, sorts them and
 * outputs a list<I>
 * @param <I>       Input type, Output is <List<O>>
 */
public class SortingPipe<I> extends CollectorPipe<I>  {

    /**
     * Comparator used to sort items passed in
     */
    private Comparator<I> mComparator;

    /**
     * Constructor
     * @param comparator    Comparator used to sort items
     */
    public SortingPipe(Comparator<I> comparator) {
        mComparator = comparator;
    }

    /**
     * Takes each item and sorts using comparator
     * @param input
     */
    @Override
    public void handleInput(I input) {
        List<I> list = getList();
        for(int i = 0; i < list.size(); i++) {
            if(mComparator.compare(input, list.get(i)) < 0) {
                list.add(i, input);
                return;
            }
        }
        list.add(input);
    }

    /**
     * @return      Default impl returns LinkedList but can be overridden
     */
    @Override
    protected List<I> createList() {
        return new LinkedList<>();
    }
}
