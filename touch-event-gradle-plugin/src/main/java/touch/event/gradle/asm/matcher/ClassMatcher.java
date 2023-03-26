package touch.event.gradle.asm.matcher;

import touch.event.gradle.asm.constant.PluginConst;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Class匹配工具类
 * @Author wangjianzhou
 * @Date 2022/6/15 12:18
 * @Version v0.9.5
 */
public class ClassMatcher {

    private static List<String> actParentClasses = new ArrayList<>();

    static {
        actParentClasses.add(PluginConst.ActivityParentInfo.v7_AppCompat_Activity);
        actParentClasses.add(PluginConst.ActivityParentInfo.androidx_AppCompat_Activity);
    }

    public static boolean matchActivityParent(String superName) {
        return actParentClasses.contains(superName);
    }
}
