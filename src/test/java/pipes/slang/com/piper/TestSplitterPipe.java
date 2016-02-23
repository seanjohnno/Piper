package pipes.slang.com.piper;

import com.slang.piper.CollectionSplitterPipe;
import com.slang.piper.Pipe;
import com.slang.piper.Piper;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by MrLenovo on 23/02/2016.
 */
public class TestSplitterPipe {

    @Test
    public void testCreate() {
        Assert.assertNotNull(Piper.<String>split());
    }

    @Test
    public void testSplitAndPass() {
        final WrappedObj<Integer> wInt = new WrappedObj<>();
        wInt.val = 0;

        Pipe<Collection<String>, String> splitter;
        (splitter = Piper.<String>split()).connect(new Pipe<String, Void>() {
            @Override
            public void handleInput(String mInput) {
                wInt.val++;
            }
        });

        List<String> l = new ArrayList<>();
        l.add("Hello");
        l.add("List");
        l.add("Splitter");
        splitter.input(l);

        Assert.assertEquals(l.size(), (int)wInt.val);
    }
}
