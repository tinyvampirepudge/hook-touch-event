package touch.event.sdk.model;

/**
 * @Description: 上报的事件格式
 * @Author wangjianzhou
 * @Date 2022/6/21 14:10
 * @Version v0.9.5
 */
public class ReportTouches {
    private float x;
    private float y;
    private String type;
    private long time;

    public ReportTouches(float x, float y, String type, long time) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.time = time;
    }

    @Override
    public String toString() {
        return "ReportTouches{" +
                "x=" + x +
                ", y=" + y +
                ", type='" + type + '\'' +
                ", time=" + time +
                '}';
    }
}
