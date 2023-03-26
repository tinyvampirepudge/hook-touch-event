package touch.event.sdk;

import touch.event.sdk.util.LogUtils;
import touch.event.sdk.model.ReportTouches;
import touch.event.sdk.model.TouchEventSequence;
import touch.event.sdk.model.ReportEvent;
import touch.event.sdk.model.TouchMotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @Description: 事件上报
 * 数据来源：{@link TouchEventClassification}
 * @Author wangjianzhou
 * @Date 2022/6/20 10:48
 * @Version v0.9.5
 */
public class TouchEventReporter {
    private static final String TAG = "TouchEvent-Reporter";

    /**
     * 上报单击、滑动、长按、CANCEL事件
     *
     * @param seq
     */
    public static void reportEvent(TouchEventSequence seq) {
        reportEvent(seq, null);
    }

    /**
     * 上报双击事件
     *
     * @param firstSeq
     * @param secondSeq
     */
    public static void reportEvent(TouchEventSequence firstSeq, TouchEventSequence secondSeq) {
        LogUtils.i(TAG, String.format("reportEvent firstSeq:%s, secondSeq:%s", firstSeq, secondSeq));
        if (firstSeq == null || !firstSeq.isValidType()) {
            LogUtils.d(TAG, "reportEvent firstSeq or secondSeq has invalid type");
            return;
        }

        ReportEvent event = new ReportEvent();
        event.active = true;
        // name
        event.name = firstSeq.getEventType();
        Map<String, Object> info = new TreeMap<>();
        // 点击为1，双击为2，长按为1
        int tapCount = 1;
        if (Objects.equals(firstSeq.getEventType(), TouchMotionEvent.EVENT_DOUBLE_CLICK)) {
            tapCount = 2;
        }
        info.put(TouchMotionEvent.KEY_TAP_COUNT, tapCount);
        // 不处理多指触控情况下，都默认为1
        info.put(TouchMotionEvent.KEY_TOUCH_COUNT, 1);
        // 事件的数据源 ——-> 数组

        // left | top | right | bottom
        String type = genSwipeType(firstSeq);

        List<ReportTouches> touchLists = new ArrayList<>();
        if (firstSeq.getStart() != null) {
            touchLists.add(convertEvent(firstSeq.getStart(), type));
        }
        for (int i = 0; i < firstSeq.getMiddle().size(); i++) {
            touchLists.add(convertEvent(firstSeq.getMiddle().get(i), type));
        }
        if (firstSeq.getEventType() != null) {
            touchLists.add(convertEvent(firstSeq.getEnd(), type));
        }
        // 双击事件的第二个序列
        if (secondSeq != null && secondSeq.isValidType()) {
            if (secondSeq.getStart() != null) {
                touchLists.add(convertEvent(secondSeq.getStart(), type));
            }
            for (int i = 0; i < secondSeq.getMiddle().size(); i++) {
                touchLists.add(convertEvent(secondSeq.getMiddle().get(i), type));
            }
            if (secondSeq.getEventType() != null) {
                touchLists.add(convertEvent(secondSeq.getEnd(), type));
            }
        }

        info.put(TouchMotionEvent.KEY_TOUCH_ARRAY, touchLists);

        event.info = info;
        LogUtils.i(TAG, String.format("reportEvent send event:%s", event));

        // TODO: 2023/3/12 00:20 发送event
    }

    private static ReportTouches convertEvent(TouchMotionEvent src, String type) {
        // type: swipe事件为必须，判断规则使用 UP -> DOWN的x/y 最大绝对值判定
        ReportTouches result = new ReportTouches(src.getX(), src.getY(), type, src.getRelativeTime());
        return result;
    }

    private static String genSwipeType(TouchEventSequence seq) {
        StringBuilder sb = new StringBuilder();
        if (Objects.equals(seq.getEventType(), TouchMotionEvent.EVENT_SWIPE)) {
            float downX = seq.getStart().getX();
            float downY = seq.getStart().getY();
            float upX = seq.getEnd().getX();
            float upY = seq.getEnd().getY();
            if (upX < downX) {
                sb.append("l");
            } else if (upX > downX) {
                sb.append("r");
            }
            if (sb.length() != 0) {
                sb.append(" | ");
            }
            if (upY < downY) {
                sb.append("t");
            } else if (upY > downY) {
                sb.append("b");
            }
        }
        return sb.toString();
    }

}
