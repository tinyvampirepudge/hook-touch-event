package touch.event.gradle.asm.utils;

/**
 * @Description: StringUtils
 * @Author wangjianzhou
 * @Date 2022/6/15 14:35
 * @Version v0.9.5
 */
public class StringUtils {

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}
