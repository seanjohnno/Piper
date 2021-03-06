package com.slang.piper;

import java.util.Comparator;

/**
 * Convenience methods to create pipes
 */
public class Piper {

    /**
     * Can be used to kick-off data flow by passing input straight into a pipe, once another pipe is
     * connected it'll be passed along
     * @param passData  Data to pass to next pipe
     * @param <O>   Output of this so input of next
     * @return      Created pipe
     */
    public static <O> SinglePipe<Void, O> start(final O passData) {
        SinglePipe<Void, O> pipe = new SinglePipe<Void, O>() {
            @Override
            protected O process(Void input) {
                return passData;
            }
        };
        pipe.input(null);
        return pipe;
    }

    /**
     * Creates a single input/output pipe. Passed in func is invoked passing in input, implementation
     * of func does the transform and passes output
     * @param func
     * @param <I>   Input type
     * @param <O>   Output type
     * @return      Created pipe
     */
    public static <I, O> Pipe<I, O> single(final Func1<I, O> func) {
        return new SinglePipe<I, O>() {
            @Override
            protected O process(I input) {
                return func.call(input);
            }
        };
    }

    /**
     * Creates a single input/output pipe. Passed in func is invoked passing in input, implementation
     * of func does the transform and passes output
     * @param func
     * @param <I>   Input type
     * @param <O>   Output type
     * @return      Created pipe
     */
    public static <I, O> SinglePipe<I, O> single(final Func1<I, O> func, Pipe.ThreadType threadType) {
        return new SinglePipe<I, O>(threadType) {
            @Override
            protected O process(I input) {
                return func.call(input);
            }
        };
    }

    /**
     * Creates a transform pipe. Passed in func is invoked passing in input, implementation
     * of func does the transform and passes output
     * @param func
     * @param <I>   Input type
     * @param <O>   Output type
     * @return      Created pipe
     */
    public static <I, O> TransformPipe<I, O> transform(final Func1<I, O> func) {
        return new TransformPipe<I, O>() {
            @Override
            protected O process(I input) {
                return func.call(input);
            }
        };
    }

    /**
     * Creates a single input/output pipe. Passed in func is invoked passing in input, implementation
     * of func does the transform and passes output
     * @param func
     * @param <I>   Input type
     * @param <O>   Output type
     * @return      Created pipe
     */
    public static <I, O> TransformPipe<I, O> transform(final Func1<I, O> func, Pipe.ThreadType threadType) {
        return new TransformPipe<I, O>(threadType) {
            @Override
            protected O process(I input) {
                return func.call(input);
            }
        };
    }

    /**
     * Creates a sorting pipe which takes multiple input (I), sorts them using the comparator passed
     * in and outputs List<I>
     * @param comparator
     * @param <I>
     * @return  Created sorting pipe
     */
    public static <I> SortingPipe<I> sort(Comparator<I> comparator) {
        return new SortingPipe<>(comparator);
    }

    /**
     * Creates a sorting pipe which takes multiple input (I), sorts them using the comparator passed
     * in and outputs List<I>
     * @param comparator
     * @param <I>
     * @return  Created sorting pipe
     */
    public static <I> SortingPipe<I> sort(Comparator<I> comparator, Pipe.ThreadType type) {
        return new SortingPipe<>(comparator, type);
    }

    /**
     * Creates a collector pipe which takes multiple input (I) ad outputs List<I>
     * @param <I>
     * @return  Created pipe
     */
    public static <I> CollectorPipe<I> collect() {
        return new CollectorPipe<I>();
    }

    /**
     * Creates a collector pipe which takes multiple input (I) ad outputs List<I>
     * @param <I>
     * @return  Created pipe
     */
    public static <I> CollectorPipe<I> collect(Pipe.ThreadType type) {
        return new CollectorPipe<I>(type);
    }

    /**
     * Creates a splitter pipe, takes a Collection<I> and passes along each item separately to next
     * pipe
     * @param <I>
     * @return  Created pipe
     */
    public static <I> CollectionSplitterPipe<I> split() {
        return new CollectionSplitterPipe<>();
    }

    /**
     * Creates a splitter pipe, takes a Collection<I> and passes along each item separately to next
     * pipe
     * @param <I>
     * @return  Created pipe
     */
    public static <I> CollectionSplitterPipe<I> split(Pipe.ThreadType type) {
        return new CollectionSplitterPipe<>(type);
    }

    /**
     * Creates a splitter pipe, takes an I[] and passes along each item separately to next pipe
     * @param <I>
     * @return  Created pipe
     */
    public static <I> ArraySplitterPipe<I> splitArr() {
        return new ArraySplitterPipe<>();
    }

    /**
     * Creates a splitter pipe, takes an I[] and passes along each item separately to next pipe
     * @param <I>
     * @return  Created pipe
     */
    public static <I> ArraySplitterPipe<I> splitArr(Pipe.ThreadType type) {
        return new ArraySplitterPipe<>(type);
    }

    public static <O> DistributorPipe<O> distributor() {
        return new DistributorPipe<O>();
    }

    public static <O> DistributorPipe<O> distributor(Pipe.ThreadType type) {
        return new DistributorPipe<O>(type);
    }

    /*------------------------------------------------------------------------------------------
     *
     ------------------------------------------------------------------------------------------*/

    /**
     * Represents function used to take input and return the output
     * @param <I>       Input type
     * @param <O>       Output type
     */
    public interface Func1<I, O> {
        public O call(I input);
    }
}
