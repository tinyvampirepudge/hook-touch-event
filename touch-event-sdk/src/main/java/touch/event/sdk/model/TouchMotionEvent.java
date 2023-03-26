package touch.event.sdk.model;

import android.os.Build;
import android.view.MotionEvent;

import androidx.annotation.StringDef;

import touch.event.sdk.TouchEventUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 装载MotionEvent信息
 * {@link android.view.MotionEvent}
 * @Author wangjianzhou
 * @Date 2022/6/20 11:04
 * @Version v0.9.5
 */
public class TouchMotionEvent {
    private static final String TAG = "UVTouchEvent-Event";

    /**
     * 单指的pointer id
     */
    public static final int POINTER_ID_SINGLE_FINGER = 0;

    private TouchActivityInfo activityInfo;

    private int action;

    private int actionButton;

    private int pointerCount;

    /**
     * getPointerId
     *
     * @param args
     */
    private List<UVPointerInfo> pointerInfos;

    private int buttonState;

    private int metaState;

    private int flags;

    private int edgeFlags;

    private int historySize;

    private long eventTime;

    private long downTime;

    private int deviceId;

    private int source;

    /**
     * 相对时间
     */
    private long relativeTime;

    public TouchMotionEvent(TouchActivityInfo activityInfo, MotionEvent src) {
        this.activityInfo = activityInfo;
        copyFromMotionEvent(src);
    }

    public void copyFromMotionEvent(MotionEvent src) {
        if (src == null) {
            return;
        }
        this.action = src.getAction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton = src.getActionButton();
        }
        pointerCount = src.getPointerCount();
        pointerInfos = new ArrayList<>(pointerCount);
        for (int i = 0; i < pointerCount; i++) {
            UVPointerInfo uvPointerInfo = new UVPointerInfo();
            uvPointerInfo.setPointerId(src.getPointerId(i));
            uvPointerInfo.setX(src.getX(i));
            uvPointerInfo.setY(src.getY(i));
            uvPointerInfo.setToolType(src.getToolType(i));
            pointerInfos.add(uvPointerInfo);
        }
        buttonState = src.getButtonState();
        metaState = src.getMetaState();
        flags = src.getFlags();
        edgeFlags = src.getEdgeFlags();
        historySize = src.getHistorySize();
        eventTime = src.getEventTime();
        downTime = src.getDownTime();
        deviceId = src.getDeviceId();
        source = src.getSource();
    }

    public TouchActivityInfo getActivityInfo() {
        return activityInfo;
    }

    public int getAction() {
        return action;
    }

    public int getActionButton() {
        return actionButton;
    }

    public int getPointerCount() {
        return pointerCount;
    }

    public List<UVPointerInfo> getPointerInfos() {
        return pointerInfos;
    }

    public int getButtonState() {
        return buttonState;
    }

    public int getMetaState() {
        return metaState;
    }

    public int getFlags() {
        return flags;
    }

    public int getEdgeFlags() {
        return edgeFlags;
    }

    public int getHistorySize() {
        return historySize;
    }

    public long getEventTime() {
        return eventTime;
    }

    public long getDownTime() {
        return downTime;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public int getSource() {
        return source;
    }

    public long getRelativeTime() {
        return relativeTime;
    }

    /**
     * 获取单指的数据-x
     *
     * @return
     */
    public float getX() {
        // 只读取单指的
        if (getPointerInfos() != null) {
            for (UVPointerInfo item : getPointerInfos()) {
                if (TouchEventUtils.isSingleFinger(item)) {
                    return item.getX();
                }
            }
        }
        return -1;
    }

    /**
     * 获取单指的数据-y
     *
     * @return
     */
    public float getY() {
        if (getPointerInfos() != null) {
            for (UVPointerInfo item : getPointerInfos()) {
                if (TouchEventUtils.isSingleFinger(item)) {
                    return item.getY();
                }
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "UVMotionEvent{" +
                "activityInfo=" + activityInfo +
                ", action=" + action +
                ", actionButton=" + actionButton +
                ", pointerCount=" + pointerCount +
                ", pointerInfos=" + pointerInfos +
                ", buttonState=" + buttonState +
                ", metaState=" + metaState +
                ", flags=0x" + Integer.toHexString(flags) +
                ", edgeFlags=0x" + Integer.toHexString(edgeFlags) +
                ", historySize=" + historySize +
                ", eventTime=" + eventTime +
                ", downTime=" + downTime +
                ", deviceId=" + deviceId +
                ", source=0x" + Integer.toHexString(source) +
                '}';
    }

    /**
     * 针对{@link MotionEvent#getPointerId(int) }获取到的信息
     */
    public static final class UVPointerInfo {
        private int pointerId;
        private float x;
        private float y;
        private int toolType;

        public int getPointerId() {
            return pointerId;
        }

        public void setPointerId(int pointerId) {
            this.pointerId = pointerId;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public int getToolType() {
            return toolType;
        }

        public void setToolType(int toolType) {
            this.toolType = toolType;
        }

        @Override
        public String toString() {
            return "UVPointerInfo{" +
                    "pointerId=" + pointerId +
                    ", x=" + x +
                    ", y=" + y +
                    ", toolType=" + toolType +
                    '}';
        }
    }

    public static final String EVENT_UNKNOWN = "unknown";
    public static final String EVENT_SWIPE = "swipe";
    public static final String EVENT_CLICK = "tap";
    public static final String EVENT_LONG_CLICK = "long_tap";
    public static final String EVENT_DOUBLE_CLICK = "double_tap";
    public static final String EVENT_CANCEL = "cancel";

    public static final String KEY_TAP_COUNT = "key_tap_count";
    public static final String KEY_TOUCH_COUNT = "key_touch_count";
    public static final String KEY_TOUCH_ARRAY = "key_touch_array";

    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({EVENT_UNKNOWN, EVENT_SWIPE, EVENT_CLICK, EVENT_LONG_CLICK, EVENT_DOUBLE_CLICK, EVENT_CANCEL})
    public @interface EventType {
    }
}
