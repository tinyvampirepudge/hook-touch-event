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
 * @Description: 事件归类
 * 数据来源：{@link TouchEventCollector}
 * 数据流向：{@link TouchEventReporter}
 * @Author wangjianzhou
 * @Date 2022/6/20 10:49
 * @Version v0.9.5
 */
public class TouchEventClassification {
    private static final String TAG = "TouchEvent-Classification";

    /**
     * 长按事件：判断UP和DOWN的时间是否大于500ms
     */
    private static final int ACTION_LONG_CLICK_SEQ_TIME_LIMIT = 500;

    /**
     * 双击事件：连续的两个click事件的 UP 的时间间隔是否小于750ms
     */
    private static final int ACTION_DOUBLE_CLICK_SEQ_TIME_LIMIT = 750;

    /**
     * 暂时未上报的事件序列。单击事件序列
     */
    private static Map<String, TouchEventSequence> stashSequenceMap = new HashMap<>();

    public static void classifySequence(TouchActivityInfo act, TouchEventSequence seq) {
        LogUtils.i(TAG, String.format("classifySequence act:%s, sequence:%s", act, seq));
        // 需要判断事件序列是否完整
        if (!verifySequence(seq)) {
            LogUtils.d(TAG, "classifySequence meet invalid sequence");
            return;
        }

        // 标记为 CANCEL 事件
        if (MotionEvent.ACTION_CANCEL == seq.getEnd().getAction()) {
            LogUtils.i(TAG, "classifySequence 确定为 CANCEL 事件序列");
            seq.setEventType(TouchMotionEvent.EVENT_CANCEL);
        } else if (seq.getMaxOffsetX() > TouchEventSequence.actionMoveCollectDisInterval
                || seq.getMaxOffsetY() > TouchEventSequence.actionMoveCollectDisInterval
                || Math.abs(seq.getStart().getX() - seq.getEnd().getX()) > TouchEventSequence.actionMoveCollectDisInterval
                || Math.abs(seq.getStart().getY() - seq.getEnd().getY()) > TouchEventSequence.actionMoveCollectDisInterval) {
            /**
             * 标记为 滑动 事件
             * ①DOWN和MOVE的距离最大差值是否大于5
             * ②DOWN和UP的距离差值是否大于5
             */
            seq.setEventType(TouchMotionEvent.EVENT_SWIPE);
            LogUtils.i(TAG, "classifySequence 确定为 滑动 事件序列");
        } else if (Math.abs(seq.getStart().getRelativeTime() - seq.getEnd().getRelativeTime()) > ACTION_LONG_CLICK_SEQ_TIME_LIMIT) {
            /**
             * 标记为 长按事件 事件
             * ①判断UP和DOWN的时间是否大于500ms
             */
            seq.setEventType(TouchMotionEvent.EVENT_LONG_CLICK);
            LogUtils.i(TAG, "classifySequence 确定为 长按 事件序列");
        }

        /**
         * CANCEL、滑动事件和长按事件
         * ①给当前事件添加上报标记，并上报
         * ②上一个事件未上报并且是单击事件，将上一个事件上报为单击事件并添加标记。
         */
        if (seq.getEventType().equals(TouchMotionEvent.EVENT_CANCEL) || seq.getEventType().equals(TouchMotionEvent.EVENT_SWIPE)
                || seq.getEventType().equals(TouchMotionEvent.EVENT_LONG_CLICK)) {
            seq.setConsumed(true);
            reportNormalEvent(seq);
            reportStashEventWithRemove(act.getActStr());
            return;
        }

        // 暂定为单击事件
        seq.setEventType(TouchMotionEvent.EVENT_CLICK);

        // 判断是否是双击事件
        TouchEventSequence stashSeq = stashSequenceMap.get(act.getActStr());
        if (stashSeq != null && !stashSeq.isConsumed() && TouchMotionEvent.EVENT_CLICK.equals(stashSeq.getEventType())
                && Math.abs(stashSeq.getEnd().getRelativeTime() - seq.getEnd().getRelativeTime()) <= ACTION_DOUBLE_CLICK_SEQ_TIME_LIMIT
                && Math.abs(stashSeq.getStart().getX() - seq.getEnd().getX()) < TouchEventSequence.getActionDownClassifyDoubleLimit()
                && Math.abs(stashSeq.getStart().getY() - seq.getEnd().getY()) < TouchEventSequence.getActionDownClassifyDoubleLimit()) {
            LogUtils.i(TAG, "classifySequence 确定为 双击 事件序列");
            // 添加标记
            stashSeq.setEventType(TouchMotionEvent.EVENT_DOUBLE_CLICK);
            stashSeq.setConsumed(true);
            seq.setEventType(TouchMotionEvent.EVENT_DOUBLE_CLICK);
            seq.setConsumed(true);
            // 清除对应缓存
            stashSequenceMap.remove(act.getActStr());
            // 上报双击事件
            reportDoubleEvent(stashSeq, seq);
        } else {
            LogUtils.i(TAG, "classifySequence 确定为 非双击 事件序列，暂不上报");
            // 上一个单击事件上报为单击事件并添加标记
            reportStashEventWithRemove(act.getActStr());
            // 当前单击事件暂不上报，并记录到暂存事件中
            stashSequenceMap.put(act.getActStr(), seq);
        }

        // 如果Activity销毁了，就把对应的暂存的事件上报 stashSequenceMap
        reportStaleStashSequence();
    }

    /**
     * 上报上一个未上报的单击事件(if exists)
     * ①添加标记，上报
     *
     * @param actStr
     */
    private static void reportStashEventWithRemove(String actStr) {
        reportStashEvent(actStr, true);
    }

    /**
     * 上报上一个未上报的单击事件(if exists)
     * ①添加标记，上报
     * ②从 缓存 中移除
     *
     * @param actStr
     * @param remove
     */
    private static void reportStashEvent(String actStr, boolean remove) {
        LogUtils.i(TAG, "reportStashEvent 上报上一个未上报的单击事件(if exists) start actStr:" + actStr);
        TouchEventSequence seq = stashSequenceMap.get(actStr);
        if (seq == null) {
            return;
        }
        if (seq.isConsumed()) {
            if (remove) stashSequenceMap.remove(actStr);
            return;
        }
        seq.setConsumed(true);
        reportNormalEvent(seq);
        if (remove) stashSequenceMap.remove(actStr);
        LogUtils.i(TAG, "reportStashEvent 上报上一个未上报的单击事件(if exists) end actStr:" + actStr);
    }

    /**
     * 上报普通事件
     *
     * @param seq
     */
    private static void reportNormalEvent(TouchEventSequence seq) {
        // TODO: 2022/11/1 9:19 PM 执行上报逻辑
//        UVTouchEventReporter.reportEvent(seq);
    }

    /**
     * 上报双击事件
     *
     * @param stashSeq
     * @param seq
     */
    private static void reportDoubleEvent(TouchEventSequence stashSeq, TouchEventSequence seq) {
        TouchEventReporter.reportEvent(stashSeq, seq);
    }

    /**
     * 验证事件序列是否合法
     * ①完整
     * ②起始的事件类型是否正确
     *
     * @param seq
     * @return
     */
    private static boolean verifySequence(TouchEventSequence seq) {
        if (seq == null || seq.getStart() == null || seq.getEnd() == null
                || MotionEvent.ACTION_DOWN != seq.getStart().getAction()
                || (MotionEvent.ACTION_UP != seq.getEnd().getAction()
                && MotionEvent.ACTION_CANCEL != seq.getEnd().getAction()
                && MotionEvent.ACTION_POINTER_UP != seq.getEnd().getAction())) {
            LogUtils.d(TAG, "verifySequence invalid sequence seq: " + seq);
            return false;
        }
        return true;
    }

    /**
     * 如果对应的Activity已经回收，则上报过期的暂存的 Sequence
     */
    private static void reportStaleStashSequence() {
        LogUtils.i(TAG, "reportStaleStashSequence");
        reportSequenceWithCondition(TouchEventCollector::isStaleEventSequence);
    }

    /**
     * 把所有暂存的单击事件上报
     */
    public static void reportStashSequences() {
        LogUtils.i(TAG, "reportStashSequences");
        reportSequenceWithCondition(null);
    }

    /**
     * 根据条件，上传暂存的单击事件
     *
     * @param condition
     */
    private static void reportSequenceWithCondition(IConditionInterface<TouchEventSequence, Boolean> condition) {
        // Create a Iterator to EntrySet of HashMap
        Iterator<Map.Entry<String, TouchEventSequence>> entryIt = stashSequenceMap.entrySet().iterator();
        // Iterate over all the elements
        while (entryIt.hasNext()) {
            Map.Entry<String, TouchEventSequence> entry = entryIt.next();
            // Check if Value associated with Key is expected
            if (condition == null || condition.condition(entry.getValue())) {
                // Remove the element
                LogUtils.i(TAG, String.format("reportSequenceWithCondition"));
                reportStashEvent(entry.getKey(), false);
                entryIt.remove();
            }
        }
    }
}
