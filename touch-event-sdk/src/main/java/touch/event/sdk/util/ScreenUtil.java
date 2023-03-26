package touch.event.sdk.util;

import android.content.Context;

import androidx.annotation.DimenRes;

/**
 * @Description: 屏幕工具类
 * @Author wangjianzhou
 * @Date 2022/6/20 16:55
 * @Version v0.9.5
 */
public class ScreenUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }
        return (int) dpValue;
    }

    /**
     * px转换dip
     */
    public static int px2dip(Context context, float px) {
        float f = context.getResources().getDisplayMetrics().density;
        return (int) (px / f);
    }

    public static int dimen2px(Context context, @DimenRes int dimen) {
        float f = context.getResources().getDimensionPixelOffset(dimen);
        return (int) (f);
    }

}
