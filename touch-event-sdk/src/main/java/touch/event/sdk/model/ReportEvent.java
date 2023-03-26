package touch.event.sdk.model;

import java.util.Map;
import java.util.TreeMap;

public class ReportEvent {

    public static final String TAG_INFO = "info";
    public static final String TAG_ERROR = "error";
    public static final String TAG_WARN = "warn";
    public static final String TAG_DEBUG = "debug";
    public static final String TAG_LOG = "log";

    public static final String EVENT_CRASH = "crash";
    public static final String EVENT_LOG = "log";

    public String name;
    public Map<String, Object> info;
    public boolean active;

    public static final ReportEvent createLogEvent(String tag, String format, Object... args) {
        ReportEvent e = new ReportEvent();
        e.name = EVENT_LOG;
        e.info = new TreeMap<>();
        e.info.put("tag", tag);
        e.info.put("text", String.format(format, args));
        return e;
    }

    public static final ReportEvent createCrashEvent(String reason, String stack) {
        ReportEvent e = new ReportEvent();
        e.name = EVENT_CRASH;
        e.info = new TreeMap<>();
        e.info.put("reason", reason);
        e.info.put("stack", stack);
        return e;
    }

    /**
     * 应用启动事件
     *
     * @return
     */
    public static final ReportEvent createPageStart() {
        ReportEvent e = new ReportEvent();
        e.name = "app";
        e.info = new TreeMap<>();
        e.info.put("type", "start");
        e.active = true;
        return e;
    }

    /**
     * 应用进入后台
     *
     * @return
     */
    public static final ReportEvent createPageSleep() {
        ReportEvent e = new ReportEvent();
        e.name = "app";
        e.info = new TreeMap<>();
        e.info.put("type", "sleep");
        e.active = true;
        return e;
    }

    /**
     * 应用进入前台
     *
     * @return
     */
    public static final ReportEvent createPageWake() {
        ReportEvent e = new ReportEvent();
        e.name = "app";
        e.info = new TreeMap<>();
        e.info.put("type", "wake");
        e.active = true;
        return e;
    }

    @Override
    public String toString() {
        return "ReportEvent{" + "name='" + name + '\'' + ", info=" + info + ", active=" + active + '}';
    }
}
