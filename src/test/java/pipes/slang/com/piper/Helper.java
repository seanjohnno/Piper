package pipes.slang.com.piper;

/**
 * Created by MrLenovo on 23/02/2016.
 */
public class Helper {

    public static WrappedString createStr() { return new WrappedString(); }
    public static class WrappedString { public String val; }

    public static WrappedBool createBool() { return new WrappedBool(); }
    public static class WrappedBool {
        public boolean val;
        public void True() {
            val = true;
        }
        public void False() {
            val = false;
        }
    }


}
