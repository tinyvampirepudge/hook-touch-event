package touch.event.gradle.asm.utils;

/**
 * @Description: sout工具类
 * @Author wangjianzhou
 * @Date 2022/6/14 16:23
 * @Version v0.9.5
 */
public class LogUtils {
    private static final String TAG = "Plugin-";

    public static void println() {
        System.out.println(TAG);
    }

    public static void println(boolean x) {
        System.out.println(TAG + x);
    }

    public static void println(char x) {
        System.out.println(TAG + x);
    }

    public static void println(int x) {
        System.out.println(TAG + x);
    }

    public static void println(long x) {
        System.out.println(TAG + x);
    }

    public static void println(float x) {
        System.out.println(TAG + x);
    }

    public static void println(double x) {
        System.out.println(TAG + x);
    }

    public static void println(char[] x) {
        System.out.println(TAG + x);
    }

    public static void println(String x) {
        System.out.println(TAG + x);
    }

    public static void println(Object x) {
        System.out.println(TAG + x);
    }

}
