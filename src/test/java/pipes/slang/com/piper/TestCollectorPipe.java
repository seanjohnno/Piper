package pipes.slang.com.piper;

import com.slang.piper.CollectorPipe;
import com.slang.piper.Pipe;
import com.slang.piper.Piper;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by MrLenovo on 23/02/2016.
 */
public class TestCollectorPipe {

    @Test
    public void testCreation() {
        CollectorPipe<String> pipe = Piper.<String>collect();
        Assert.assertNotNull(pipe);
    }

    @Test
    public void testCollect() {

        final WrappedObj<List<String>> output = new WrappedObj<>();
        Pipe start;
        (start = Piper.<String>collect()).connect(new Pipe<List<String>, Void>() {

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

        // Check our output list matches our input array
        for(int i = 0; i < arr.length; i++) {
            Assert.assertEquals(arr[i], output.val.get(i));
        }
    }
}
