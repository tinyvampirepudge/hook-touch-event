package touch.event.sdk.model;

import android.app.Activity;
import android.view.MotionEvent;

import touch.event.sdk.util.LogUtils;
import touch.event.sdk.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description: 完整的事件序列
 * 以DOWN开始，以UP或者CANCEL结束
 * @Author wangjianzhou
 * @Date 2022/6/20 14:45
 * @Version v0.9.5
 */
public class TouchEventSequence {
    private static final String TAG = "TouchEvent-Sequence";

    /**
     * ACTION_MOVE事件采集时间间隔，50ms
     */
    private static final int ACTION_MOVE_COLLECT_TIME_INTERVAL = 50;
    /**
     * ACTION_MOVE事件采集空间间隔，5dp
     */
    private static final int ACTION_MOVE_COLLECT_DIS_LIMIT = 5;
    // dp 转 px
    public static int actionMoveCollectDisInterval = ACTION_MOVE_COLLECT_DIS_LIMIT;

    /**
     * double事件判断：两个click事件的空间间隔，10dp
     */
    private static final int ACTION_DOWN_CLASSIFY_DOUBLE_LIMIT = 10;
    // dp 转 px
    public static int actionDownClassifyDoubleLimit = ACTION_DOWN_CLASSIFY_DOUBLE_LIMIT;

    private TouchMotionEvent start;
    private List<TouchMotionEvent> middle = new ArrayList<>();
    private TouchMotionEvent end;

    private String seqId;

    /**
     * 是否被消费过
     */
    private boolean consumed;

    private float lastX;
    private float lastY;
    private long lastTimestamp;

    private float maxOffsetX;
    private float maxOffsetY;

    /**
     * 事件序列的类型
     */
    private @TouchMotionEvent.EventType
    String eventType = TouchMotionEvent.EVENT_UNKNOWN;

    public static void initDis(Activity act) {
        if (actionMoveCollectDisInterval == ACTION_MOVE_COLLECT_DIS_LIMIT) {
            actionMoveCollectDisInterval = ScreenUtil.dip2px(act, ACTION_MOVE_COLLECT_DIS_LIMIT);
        }
        if (actionDownClassifyDoubleLimit == ACTION_DOWN_CLASSIFY_DOUBLE_LIMIT) {
            actionDownClassifyDoubleLimit = ScreenUtil.dip2px(act, ACTION_DOWN_CLASSIFY_DOUBLE_LIMIT);
        }
    }

    public TouchEventSequence() {
        seqId = UUID.randomUUID().toString();
    }

    public TouchMotionEvent getStart() {
        return start;
    }

    public void setStart(TouchMotionEvent event) {
        LogUtils.i(TAG, String.format("setStart event:%s", event));
        this.start = event;
        updateEventInfo(event);
    }

    public List<TouchMotionEvent> getMiddle() {
        return middle;
    }

    public void addMiddle(TouchMotionEvent event) {
        LogUtils.i(TAG, String.format("addMiddle event:%s", event));
        // 距离上次采集事件的时间间隔超过50ms 或者
        // x/y的移动距离超过5
        if (middle.size() != 0) {
            if (Math.abs(lastTimestamp - event.getRelativeTime()) < ACTION_MOVE_COLLECT_TIME_INTERVAL
                    && Math.abs(lastX - event.getX()) < actionMoveCollectDisInterval
                    && Math.abs(lastY - event.getY()) < actionMoveCollectDisInterval) {
                LogUtils.d(TAG, String.format("addMiddle skip, lastX:%s,lastY:%s, lastTimestamp:%s, event:%s",
                        lastX, lastY, lastTimestamp, event));
                return;
            }
        }
        middle.add(event);
        updateEventInfo(event);
    }

    public TouchMotionEvent getEnd() {
        return end;
    }

    public void setEnd(TouchMotionEvent event) {
        LogUtils.i(TAG, String.format("setEnd event:%s", event));
        this.end = event;
        updateEventInfo(event);
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public float getMaxOffsetX() {
        return maxOffsetX;
    }

    public float getMaxOffsetY() {
        return maxOffsetY;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public static int getActionDownClassifyDoubleLimit() {
        return actionDownClassifyDoubleLimit;
    }

    private void updateEventInfo(TouchMotionEvent event) {
        lastX = event.getX();
        lastY = event.getY();
        lastTimestamp = event.getRelativeTime();
        if (MotionEvent.ACTION_DOWN != event.getAction() && getStart() != null) {
            maxOffsetX = Math.abs(event.getX() - getStart().getX());
            maxOffsetY = Math.abs(event.getY() - getStart().getY());
        }
    }

    public boolean isValidType() {
        return TouchMotionEvent.EVENT_SWIPE.equals(eventType)
                || TouchMotionEvent.EVENT_CLICK.equals(eventType)
                || TouchMotionEvent.EVENT_LONG_CLICK.equals(eventType)
                || TouchMotionEvent.EVENT_CANCEL.equals(eventType)
                || TouchMotionEvent.EVENT_DOUBLE_CLICK.equals(eventType);
    }

    @Override
    public String toString() {
        return "TouchEventSequence{" +
                "start=" + start +
                ", middle=" + middle +
                ", end=" + end +
                ", seqId='" + seqId + '\'' +
                ", consumed=" + consumed +
                ", lastX=" + lastX +
                ", lastY=" + lastY +
                ", lastTimestamp=" + lastTimestamp +
                ", maxOffsetX=" + maxOffsetX +
                ", maxOffsetY=" + maxOffsetY +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
