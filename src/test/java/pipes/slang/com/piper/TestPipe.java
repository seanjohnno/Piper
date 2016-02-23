package pipes.slang.com.piper;

import android.os.Handler;

import com.slang.piper.Pipe;
import com.slang.piper.Piper;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Executor;

/**
 * Created by MrLenovo on 23/02/2016.
 */
public class TestPipe {

    @Test
    public void testPipeCreation() {
        Pipe p = Piper.single(null);
        Assert.assertNotNull(p);
    }

    @Test
    public void testPipeInput() {

        final Helper.WrappedBool called = Helper.createBool();
        Pipe<String,String> p = Piper.single(new Piper.Func1<String, String>() {
            @Override
            public String call(String input) {
                called.True();
                return input;
            }
        });

        p.input("Hello");
        Assert.assertTrue(called.val);
    }

    @Test
    public void testPipeOutput() {
        final Helper.WrappedString str = Helper.createStr();

        Pipe<String, String> start = Piper.single(new Piper.Func1<String, String>() {
            public String call(String input) {
                return input;
            }
        });
        start.connect(Piper.single(new Piper.Func1<String, Void>() {
            public Void call(String input) {
                str.val = input;
                return null;
            }
        }));

        String testStr = "Hello";
        start.input(testStr);
        Assert.assertEquals(str.val, testStr);
    }

    @Test
    public void testModifyOutput() {
        final Helper.WrappedString str = Helper.createStr();

        Pipe<String, String> start = Piper.single(new Piper.Func1<String, String>() { public String call(String input) {
            return input.toUpperCase();
        }});
        start.connect(Piper.single(new Piper.Func1<String, Void>() {
            public Void call(String input) {
                str.val = input;
                return null;
            }
        }));

        String testStr = "Hello";
        start.input(testStr);
        Assert.assertEquals(str.val, testStr.toUpperCase());
    }


    /**
     * Should receive the data on connect
     */
    @Test
    public void testBelatedConnect() {
        final Helper.WrappedString str = Helper.createStr();

        Pipe<String, String> start = Piper.single(new Piper.Func1<String, String>() {
            public String call(String input) {
                return input;
            }
        });

        // Pass in data
        String testStr = "Hello";
        start.input(testStr);

        // Then connect pipe
        start.connect(Piper.single(new Piper.Func1<String, Void>() {
            public Void call(String input) {
                str.val = input;
                return null;
            }
        }));

        Assert.assertEquals(str.val, testStr);
    }

    @Test
    public void testMainThread() {
        final WrappedObj<Boolean> b = new WrappedObj<>();
        b.val = false;

        Piper.start("").connect(new Pipe<String, Void>() {
            @Override
            protected void handleInput(String mInput) {
                b.val = true;
            }
        });

        Assert.assertTrue(b.val);
    }

    @Test
    public void testIOThread() {
        final WrappedObj<Boolean> b = new WrappedObj<>();
        b.val = false;

        Piper.start("").connect(new Pipe<String, Void>() {
            @Override
            protected void handleInput(String mInput) {
                b.val = true;
            }

            @Override
            protected Executor getIOExecutor() {
                return new Executor() {
                    public void execute(Runnable r) {
                        r.run();
                    }
                };
            }
        } );

        Assert.assertTrue(b.val);
    }
}
