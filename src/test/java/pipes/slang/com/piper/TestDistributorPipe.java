package pipes.slang.com.piper;

import com.slang.piper.DistributorPipe;
import com.slang.piper.Piper;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by MrLenovo on 23/02/2016.
 */
public class TestDistributorPipe {

    @Test
    public void testCreation() {
        Assert.assertNotNull(Piper.distributor());
    }

    @Test
    public void testDistribute() {
        final WrappedObj<Integer> wo = new WrappedObj<>();
        wo.val = 0;
        Piper.Func1<String, Void> func = new Piper.Func1<String, Void>() {
            @Override
            public Void call(String input) {
                wo.val++;
                return null;
            }
        };

        DistributorPipe<String> dis = Piper.<String>distributor();
        dis.connect(Piper.pipe(func));
        dis.connect(Piper.pipe(func));
        dis.connect(Piper.pipe(func));
        dis.connect(Piper.pipe(func));
        Piper.start("Hello").connect(dis);

        Assert.assertEquals(4, (int)wo.val);
    }
}
