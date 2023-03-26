package touch.event.gradle.asm.visitor;

import touch.event.gradle.asm.matcher.ClassInfo;
import touch.event.gradle.asm.matcher.MethodMatcher;
import touch.event.gradle.asm.utils.LogUtils;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;

/**
 * @Description: ClassNode获取
 * @Author wangjianzhou
 * @Date 2022/6/15 09:55
 * @Version v0.9.5
 */
public class TouchEventClassNode extends ClassNode {
    private static final String TAG = "ClassNode";

    private String className;
    private String superName;

    private ITargetMethodMatchedListener targetMethodMatchedListener;

    public TouchEventClassNode() {
    }

    public TouchEventClassNode(ITargetMethodMatchedListener listener) {
        super(Opcodes.ASM4);
        this.targetMethodMatchedListener = listener;
        // public List<MethodNode> methods;
        LogUtils.println(String.format("%s, constructor methods:%s", TAG, methods));
    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
        LogUtils.println(String.format("%s, visit version:%s, access:%d, name:%s, signature:%s, superName:%s, interfaces:%s",
                TAG, version, access, name, signature, superName, Arrays.toString(interfaces)));
        LogUtils.println(String.format("%s, visit methods:%s", TAG, methods));
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        LogUtils.println(TAG + " visitMethod name-------" + name + ", superName is " + superName);
        LogUtils.println(String.format("%s, visitMethod superName:%s, access:%d, name:%s, descriptor:%s, signature:%s, exceptions:%s",
                TAG, superName, access, name, descriptor, signature, Arrays.toString(exceptions)));

        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        LogUtils.println(TAG + " visitEnd start");
        LogUtils.println(String.format("%s, visitEnd methods:%s", TAG, methods));
        // 每个方法对应的visitMethod方法被调用后，methods中才会有数据。
        //  在这里判断是否有对应的方法。如果没有，就添加对应方法。
        boolean match = false;
        for (MethodNode m : methods) {
            LogUtils.println(String.format("%s, visitEnd access:%d, name:%s, desc:%s, signature:%s, exceptions:%s, parameters:%s, instructions:%s, maxStack:%s, maxLocals:%s, localVariables:%s",
                    TAG, m.access, m.name, m.desc, m.signature, m.exceptions, m.parameters, m.instructions, m.maxStack, m.maxLocals, m.localVariables));
            if (MethodMatcher.matchDispatchMethodNode(m)) {
                match = true;
                break;
            }
        }
        LogUtils.println(String.format("%s, visitEnd match:%s", TAG, match));
        if (targetMethodMatchedListener != null) {
            ClassInfo classInfo = new ClassInfo(className, superName);
            targetMethodMatchedListener.onMatch(match, classInfo);
        }

        super.visitEnd();
        LogUtils.println(TAG + " visitEnd end");
    }

}
