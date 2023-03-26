package touch.event.gradle.asm.matcher;

/**
 * @Description: Class相关信息
 * @Author wangjianzhou
 * @Date 2022/6/15 12:19
 * @Version v0.9.5
 */
public class ClassInfo {
    public String className;
    public String superName;

    public ClassInfo() {
    }

    public ClassInfo(String className, String superName) {
        this.className = className;
        this.superName = superName;
    }
}
