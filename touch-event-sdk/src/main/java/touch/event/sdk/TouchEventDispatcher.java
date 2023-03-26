package touch.event.sdk;

import android.app.Activity;
import android.view.MotionEvent;

import touch.event.sdk.util.LogUtils;
import touch.event.sdk.model.TouchActivityInfo;
import touch.event.sdk.model.TouchMotionEvent;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @Description: 字节码中调用。不要混淆，需keep住
 * 接收到事件后的核心步骤：
 * 事件采集：{@link TouchEventCollector}
 * 事件分类：{@link TouchEventClassification}
 * 事件上报：{@link TouchEventReporter}
 * @Author wangjianzhou
 * @Date 2022/6/13 13:51
 * @Version
 */
public class TouchEventDispatcher {
    private static final String TAG = TouchEventDispatcher.class.getSimpleName();

    /**
     * 事件从字节码转发过来
     * hook住的数据，会传递给这个方法
     *
     * @param className：类名
     * @param methodName：方法名称
     * @param params：参数列表，依次是Activity和MotionEvent对象
     */
    public static void dispatchTouchEventParams(String className, String methodName, List params) {
        LogUtils.i(TAG, String.format("dispatchTouchEventParams act:%s, event:%s", className, methodName));
        // 校验数据
        if (params == null || params.isEmpty() || params.size() < 2) {
            LogUtils.d(TAG, "dispatchTouchEventParams params is invalid");
            return;
        }
        Activity actRef = null;
        MotionEvent eventRef = null;
        // 获取数据
        Object actObj = params.get(0);
        if (actObj != null && actObj instanceof Activity) {
            actRef = (Activity) actObj;
        }
        Object eventObj = params.get(1);
        if (eventObj != null && eventObj instanceof MotionEvent) {
            eventRef = (MotionEvent) eventObj;
        }
        if (actRef == null || eventRef == null) {
            LogUtils.d(TAG, "dispatchTouchEventParams actRef or eventRef is invalid");
            return;
        }

        LogUtils.i(TAG, String.format("dispatchTouchEventParams actRef:%s, eventRef:%s", actRef, eventRef));

        // 将Activity强引用转换成weakReference；MotionEvent数据进行copy。
        TouchActivityInfo touchActivityInfo = new TouchActivityInfo(actRef.toString(), new WeakReference<>(actRef));
        TouchMotionEvent uvEvent = convertMotionEvent(touchActivityInfo, eventRef);

        // 事件采集
        TouchEventCollector.collectTouchEvent(touchActivityInfo, uvEvent);
    }


    private static TouchMotionEvent convertMotionEvent(TouchActivityInfo touchActivityInfo, MotionEvent event) {
        if (event == null) return null;
        return new TouchMotionEvent(touchActivityInfo, event);
    }
}
