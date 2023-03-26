package touch.event.gradle.asm.visitor;


import touch.event.gradle.asm.matcher.ClassMatcher;
import touch.event.gradle.asm.utils.LogUtils;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;

/**
 * @Description: 处理项目中，Activity的直接子类
 * @Author wangjianzhou
 * @Date 2022/6/13 18:30
 * @Version v0.9.5
 */
public class TouchEventClassVisitor extends ClassVisitor {
    private static final String TAG = "ClassVisitor";

    private String className;
    private String superName;

    public TouchEventClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM4, classVisitor);
    }

    public TouchEventClassVisitor(ClassVisitor classVisitor, String className) {
        super(Opcodes.ASM4, classVisitor);
        this.className = className;
    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        LogUtils.println(TAG + " visitMethod name-------" + name + ", superName is " + superName);
        LogUtils.println(String.format("%s, visitMethod superName:%s, access:%d, name:%s, descriptor:%s, signature:%s, exceptions:%s",
                TAG, superName, access, name, descriptor, signature, exceptions != null ? Arrays.toString(exceptions) : "null"));
        /*
            output:
            Plugin-ClassVisitor, visitMethod superName:androidx/appcompat/app/AppCompatActivity, access:1, name:dispatchTouchEvent, descriptor:(Landroid/view/MotionEvent;)Z, signature:null, exceptions:null
         */

        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);

        // androidx 和 support
        // TODO: 2022/6/14 18:27 这里是否可以扩大范围，比如FragmentActivity；是否还能支持用户自行配置
        if (ClassMatcher.matchActivityParent(superName)) {
            //判断方法
            // 判断方法修饰符、方法参数
            if ("dispatchTouchEvent".equals(name) && access == Opcodes.ACC_PUBLIC && "(Landroid/view/MotionEvent;)Z".equals(descriptor)) {
                LogUtils.println(TAG + " filter to dispatchTouchEvent");
                //处理 dispatchTouchEvent 方法
                return new TouchEventAdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, descriptor, className);
            }
        }

        return methodVisitor;
    }
}
