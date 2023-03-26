package touch.event.sdk.model;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * @Description: 承载Activity
 * @Author wangjianzhou
 * @Date 2022/6/20 10:58
 * @Version v0.9.5
 */
public class TouchActivityInfo {
    private String actStr; // activity引用的toString
    private WeakReference<Activity> weakAct;

    public TouchActivityInfo(String actStr, WeakReference<Activity> weakAct) {
        this.actStr = actStr;
        this.weakAct = weakAct;
    }

    public String getActStr() {
        return actStr;
    }

    public WeakReference<Activity> getWeakAct() {
        return weakAct;
    }

    @Override
    public String toString() {
        return "TouchActivityInfo{" +
                "actStr='" + actStr + '\'' +
                ", weakAct=" + weakAct +
                '}';
    }
}
