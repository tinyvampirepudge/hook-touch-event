package touch.event.sdk;

/**
 * @Description: 暴露给外部的Manager
 * @Author wangjianzhou
 * @Date 2022/6/21 15:55
 * @Version v0.9.5
 */
public class TouchEventManager {
    /**
     * 把暂存的单击事件序列上报
     */
    public static void reportStashSequences() {
        TouchEventClassification.reportStashSequences();
    }
}
