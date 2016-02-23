package pipes.slang.com.piper;

import com.slang.piper.Pipe;
import com.slang.piper.Piper;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by MrLenovo on 23/02/2016.
 */
public class TestArrSplitterPipe {
    @Test
    public void testCreate() {
        Assert.assertNotNull(Piper.<String>splitArr());
    }

    @Test
    public void testSplitAndPass() {
        final WrappedObj<Integer> wInt = new WrappedObj<>();
        wInt.val = 0;

        Pipe<String[], String> splitter;
        (splitter = Piper.<String>splitArr()).connect(new Pipe<String, Void>() {
            @Override
            public void handleInput(String mInput) {
                wInt.val++;
            }
        });

        String[] l = {"Hello", "List", "Splitter" };
        splitter.handleInput(l);

        Assert.assertEquals(l.length, (int)wInt.val);
    }
}
