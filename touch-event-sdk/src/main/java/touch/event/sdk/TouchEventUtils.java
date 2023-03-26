package touch.event.sdk;

import touch.event.sdk.model.TouchMotionEvent;

import java.util.List;

/**
 * @Description: 工具类
 * @Author wangjianzhou
 * @Date 2022/6/21 10:35
 * @Version v0.9.5
 */
public class TouchEventUtils {

    /**
     * 是否是单指的数据
     *
     * @param pointerInfo
     * @return
     */
    public static boolean isSingleFinger(TouchMotionEvent.UVPointerInfo pointerInfo) {
        return pointerInfo != null && pointerInfo.getPointerId() == TouchMotionEvent.POINTER_ID_SINGLE_FINGER;
    }

    /**
     * 是否包含单指的数据
     *
     * @param list
     * @return
     */
    public static boolean containsSingleFinger(List<TouchMotionEvent.UVPointerInfo> list) {
        if (list != null && !list.isEmpty()) {
            for (TouchMotionEvent.UVPointerInfo item : list) {
                if (isSingleFinger(item)) {
                    return true;
                }
            }
        }
        return false;
    }
}
