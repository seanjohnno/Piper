package pipes.slang.com.piper;

import com.slang.piper.Pipe;
import com.slang.piper.Piper;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by MrLenovo on 23/02/2016.
 */
public class TestChainingAll {

    @Test
    public void testChain() {

        final WrappedObj<Integer> count = new WrappedObj<>();
        count.val = 0;
        final WrappedObj<String> res = new WrappedObj<>();

        String[] data = new String[] { "Lets", "pass", "a", "bunch", "of", "strings", "in", "and", "manipulate", "them" };
        Piper.<String[]>start(data)
                .connect(Piper.<String>splitArr())
                .connect(Piper.<String, String>pipe(new Piper.Func1<String, String>() {
                    @Override
                    public String call(String input) {
                        return input.toUpperCase();
                    }
                }) )
                .connect(Piper.sort(String.CASE_INSENSITIVE_ORDER))
                .connect(Piper.<String>split())
                .connect(new Pipe<String, Void>() {
                    @Override
                    public void handleInput(String input) {
                        count.val++;
                        if(res.val  != null) {
                            Assert.assertTrue(input.compareTo(res.val) >= 0);
                        }
                        res.val = input;
                    }
                });
    }
}
