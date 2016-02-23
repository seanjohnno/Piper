package pipes.slang.com.piper;

import com.slang.piper.CollectorPipe;
import com.slang.piper.Pipe;
import com.slang.piper.Piper;
import com.slang.piper.SortingPipe;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by MrLenovo on 23/02/2016.
 */
public class TestSorterPipe {
    @Test
    public void testCreation() {
        SortingPipe<String> pipe = Piper.<String>sort(null);
        Assert.assertNotNull(pipe);
    }

    @Test
    public void testCollect() {

        final WrappedObj<Integer> output = new WrappedObj<>();
        Pipe start;
        (start = Piper.<String>sort(String.CASE_INSENSITIVE_ORDER)).connect(new Pipe<List<String>, Void>() {

            @Override
            public void handleInput(List<String> input) {
                output.val = input.size();
            }
        });

        // Pass all items of array to pipe
        String[] arr = {"Do", "you", "work", "Mr", "Collector", "Pipe", "?"};
        for(String item : arr) {
            start.handleInput(item);
        }
        start.handleInputComplete();

        // Check our output list matches our input array
        for(int i = 0; i < arr.length; i++) {
            Assert.assertEquals(arr.length, (int)output.val);
        }
    }

    @Test
    public void testSort() {

        final WrappedObj<List<String>> output = new WrappedObj<>();
        Pipe start;
        (start = Piper.<String>sort(String.CASE_INSENSITIVE_ORDER)).connect(new Pipe<List<String>, Void>() {

            @Override
            public void handleInput(List<String> input) {
                output.val = input;
            }
        });

        // Pass all items of array to pipe
        String[] arr = {"Do", "you", "work", "Mr", "Collector", "Pipe", "?"};
        for(String item : arr) {
            start.handleInput(item);
        }
        start.handleInputComplete();

        // Check our array has been sorted correctly
        for(int i = 1; i < output.val.size(); i++) {
            Assert.assertTrue(output.val.get(i).compareTo(output.val.get(i-1)) >= 0 );
        }
    }
}
