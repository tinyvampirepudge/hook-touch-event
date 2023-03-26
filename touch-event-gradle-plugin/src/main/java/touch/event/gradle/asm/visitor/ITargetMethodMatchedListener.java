package touch.event.gradle.asm.visitor;

import touch.event.gradle.asm.matcher.ClassInfo;

/**
 * @Description: 获取ClassNode中，通过methods匹配目标方法的结果
 * @Author wangjianzhou
 * @Date 2022/6/15 16:57
 * @Version v0.9.5
 */
public interface ITargetMethodMatchedListener {
    void onMatch(boolean matched, ClassInfo classInfo);
}
