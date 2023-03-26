package touch.event.sdk;

import android.view.MotionEvent;

import touch.event.sdk.util.LogUtils;
import touch.event.sdk.model.TouchActivityInfo;
import touch.event.sdk.model.TouchMotionEvent;
import touch.event.sdk.model.TouchEventSequence;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description: 采集事件序列
 * 数据来源：{@link TouchEventDispatcher}
 * 数据流向：{@link TouchEventClassification}
 * @Author wangjianzhou
 * @Date 2022/6/20 10:48
 * @Version v0.9.5
 */
public class TouchEventCollector {
    private static final String TAG = TouchEventCollector.class.getSimpleName();

    private static Map<String, TouchEventSequence> sequenceMap = new HashMap<>();

    public static void collectTouchEvent(TouchActivityInfo act, TouchMotionEvent event) {
        LogUtils.i(TAG, String.format("collectTouchEvent act:%s, event:%s", act, event));

        // 清理掉过期的缓存
        cleanStaleSequence();

        // 校验event
        if (act == null || event == null) {
            return;
        }

        // 初始化Context相关的常量
        TouchEventSequence.initDis(act.getWeakAct().get());

        // 只包含多指数据的Event需要剔除掉
        if (!TouchEventUtils.containsSingleFinger(event.getPointerInfos())) {
            LogUtils.i(TAG, "collectTouchEvent event.getPointerInfos() contains not single finger data");
            return;
        }

        // 核心
        /**
         * 多指的场景：A、B两个手指。
         * ①ACTION_DOWN --> ACTION_UP：A先按下，B后按下，B先抬起，A最后抬起。
         * ②ACTION_DOWN --> ACTION_POINTER_UP(0)：A先按下，B后按下，A先抬起，B最后抬起。
         */
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /*
                 * 开启新的事件序列
                 * ①原有的事件序列发送给下一个环节处理，清除对应的缓存
                 * ②创建事件序列，并添加进缓存中
                 */
                sendSequence2Next(act);
                addEvent2Sequence(act, event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                addAndSend(act, event);
                break;
            case MotionEvent.ACTION_POINTER_UP: // ACTION_POINTER_UP(0)
                //  参考 MotionEvent#toString
                int index = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                if (index == 0) {
                    addAndSend(act, event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                addEvent2Sequence(act, event);
                break;
            default: // 丢弃
                // no-op
                break;
        }
    }

    private static void addAndSend(TouchActivityInfo act, TouchMotionEvent event) {
        addEvent2Sequence(act, event);
        sendSequence2Next(act);
    }

    /**
     * 原有的事件序列发送给下一个环节处理，并清除对应的缓存
     *
     * @param act
     */
    private static void sendSequence2Next(TouchActivityInfo act) {
        LogUtils.i(TAG, "sendSequence2Next");
        TouchEventSequence sequence = sequenceMap.remove(act.getActStr());
        if (sequence == null) {
            return;
        }
        // 发送给下一个环节处理
        TouchEventClassification.classifySequence(act, sequence);
    }

    /**
     * 创建事件序列，并添加进缓存中
     *
     * @param act
     * @return
     */
    private static TouchEventSequence createSequence(TouchActivityInfo act) {
        LogUtils.i(TAG, "createSequence");
        TouchEventSequence sequence = new TouchEventSequence();
        sequenceMap.put(act.getActStr(), sequence);
        return sequence;
    }

    /**
     * 将事件添加进事件序列
     *
     * @param act
     * @param event
     */
    private static void addEvent2Sequence(TouchActivityInfo act, TouchMotionEvent event) {
        TouchEventSequence seq = sequenceMap.get(act.getActStr());
        if (seq == null) {
            seq = createSequence(act);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                seq.setStart(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP: // 多指情况下结束
                seq.setEnd(event);
                break;
            case MotionEvent.ACTION_MOVE:
                seq.addMiddle(event);
                break;
            default: // 丢弃
                // no-op
                break;
        }
    }

    /**
     * 如果对应的Activity已经回收，则清除过期的Sequence
     */
    private static void cleanStaleSequence() {
        // Create a Iterator to EntrySet of HashMap
        Iterator<Map.Entry<String, TouchEventSequence>> entryIt = sequenceMap.entrySet().iterator();
        // Iterate over all the elements
        while (entryIt.hasNext()) {
            Map.Entry<String, TouchEventSequence> entry = entryIt.next();
            // Check if Value associated with Key is expected
            if (isStaleEventSequence(entry.getValue())) {
                // Remove the element
                LogUtils.i(TAG, String.format("cleanStaleSequence stale:%s", entry.getValue()));
                entryIt.remove();
            }
        }
    }

    /**
     * 判断Event序列是否过期：对应的Activity是否被回收
     *
     * @param sequence
     * @return
     */
    public static boolean isStaleEventSequence(TouchEventSequence sequence) {
        // 数据异常，无法判断对应的Activity没有被回收。
        if (sequence == null || sequence.getStart() == null) {
            return false;
        }
        // 判断对应的Activity是否被回收
        if (sequence.getStart().getActivityInfo() == null
                || sequence.getStart().getActivityInfo().getWeakAct() == null ||
                sequence.getStart().getActivityInfo().getWeakAct().get() == null) {
            LogUtils.d(TAG, "isStaleMotionEvent stale event");
            return true;
        }
        return false;
    }
}
