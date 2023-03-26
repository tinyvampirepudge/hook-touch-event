package touch.event.gradle.asm.matcher;

import touch.event.gradle.asm.constant.PluginConst;

import org.objectweb.asm.tree.MethodNode;

/**
 * @Description:
 * @Author wangjianzhou
 * @Date 2022/6/15 12:19
 * @Version v0.9.5
 */
public class MethodMatcher {
    /**
     * {@link Activity#dispatchTouchEvent(MotionEvent ev)}
     */
    public static final MethodInfo dispatchTouchEventMethodInfo = new MethodInfo(
            PluginConst.DispatchMethodInfo.access,
            PluginConst.DispatchMethodInfo.name,
            PluginConst.DispatchMethodInfo.desc
    );

    public static boolean matchDispatchMethodNode(MethodNode m) {
        if (m == null) {
            return false;
        }
        return matchDispatchMethodInfo(new MethodInfo(m.access, m.name, m.desc, m.signature, m.exceptions, m.parameters));
    }

    public static boolean matchDispatchMethodInfo(MethodInfo m) {
        MethodInfo methodInfo = dispatchTouchEventMethodInfo;
        if (methodInfo != null && methodInfo.access == m.access && methodInfo.name.equals(m.name) && methodInfo.desc.equals(m.desc)) {
            return true;
        }
        return false;
    }
}
