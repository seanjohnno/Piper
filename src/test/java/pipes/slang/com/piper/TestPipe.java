package pipes.slang.com.piper;

import com.slang.piper.Pipe;
import com.slang.piper.Piper;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by MrLenovo on 23/02/2016.
 */
public class TestPipe {

    @Test
    public void testPipeCreation() {
        Pipe p = Piper.pipe(null);
        Assert.assertNotNull(p);
    }

    @Test
    public void testPipeInput() {

        final Helper.WrappedBool called = Helper.createBool();
        Pipe<String,String> p = Piper.pipe(new Piper.Func1<String, String>() {
            @Override
            public String call(String input) {
                called.True();
                return input;
            }
        });

        p.handleInput("Hello");
        Assert.assertTrue(called.val);
    }

    @Test
    public void testPipeOutput() {
        final Helper.WrappedString str = Helper.createStr();

        Pipe<String, String> start = Piper.pipe(new Piper.Func1<String, String>() {
            public String call(String input) {
                return input;
            }
        });
        start.connect(Piper.pipe(new Piper.Func1<String, Void>() {
            public Void call(String input) {
                str.val = input;
                return null;
            }
        }));

        String testStr = "Hello";
        start.handleInput(testStr);
        Assert.assertEquals(str.val, testStr);
    }

    @Test
    public void testModifyOutput() {
        final Helper.WrappedString str = Helper.createStr();

        Pipe<String, String> start = Piper.pipe(new Piper.Func1<String, String>() { public String call(String input) {
            return input.toUpperCase();
        }});
        start.connect(Piper.pipe(new Piper.Func1<String, Void>() {
            public Void call(String input) {
                str.val = input;
                return null;
            }
        }));

        String testStr = "Hello";
        start.handleInput(testStr);
        Assert.assertEquals(str.val, testStr.toUpperCase());
    }


    /**
     * Should receive the data on connect
     */
    @Test
    public void testBelatedConnect() {
        final Helper.WrappedString str = Helper.createStr();

        Pipe<String, String> start = Piper.pipe(new Piper.Func1<String, String>() {
            public String call(String input) {
                return input;
            }
        });

        // Pass in data
        String testStr = "Hello";
        start.handleInput(testStr);

        // Then connect pipe
        start.connect(Piper.pipe(new Piper.Func1<String, Void>() {
            public Void call(String input) {
                str.val = input;
                return null;
            }
        }));

        Assert.assertEquals(str.val, testStr);
    }
}
