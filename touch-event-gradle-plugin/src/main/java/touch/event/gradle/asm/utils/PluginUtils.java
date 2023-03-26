package touch.event.gradle.asm.utils;

import touch.event.gradle.asm.constant.PluginConst;
import touch.event.gradle.asm.matcher.ClassInfo;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 工具类
 * @Author wangjianzhou
 * @Date 2022/6/13 19:52
 * @Version v0.9.5
 */
public class PluginUtils {
    private static final String TAG = "Utils";


    public static List<String> getParameterTypeList(String descriptor) {
        List<String> parameterTypeList = new ArrayList<>();
        Matcher m =
                Pattern.compile("(L.*?;|\\[{0,2}L.*?;|[ZCBSIFJD]|\\[{0,2}[ZCBSIFJD]{1})")
                        .matcher(descriptor.substring(0, descriptor.lastIndexOf(')') + 1));

        while (m.find()) {
            parameterTypeList.add(m.group(1));
        }
        return parameterTypeList;
    }

    /**
     * 生成一个新的ArrayList对象，同时返回
     *
     * @param mv
     * @param localVariablesSorter
     * @return
     */
    public static int newParameterArrayList(MethodVisitor mv, LocalVariablesSorter localVariablesSorter) {
        /*
            ASM指令：
            NEW java/util/ArrayList
            DUP
            INVOKESPECIAL java/util/ArrayList.<init> ()V
            ASTORE 5
         */
        mv.visitTypeInsn(AdviceAdapter.NEW, "java/util/ArrayList");
        mv.visitInsn(AdviceAdapter.DUP);
        mv.visitMethodInsn(
                AdviceAdapter.INVOKESPECIAL,
                "java/util/ArrayList",
                "<init>",
                "()V",
                false
        );
        int parametersIdentifier = localVariablesSorter.newLocal(Type.getType(List.class));
        mv.visitVarInsn(AdviceAdapter.ASTORE, parametersIdentifier);
        return parametersIdentifier;
    }

    /**
     * 获取调用参数this，添加进list中
     *
     * @param mv
     * @param parametersIdentifier
     */
    public static void fillThisParam(MethodVisitor mv, int parametersIdentifier) {
         /*
            参考ASM指令：
            ALOAD 5
            ALOAD 4
            INVOKEINTERFACE java/util/List.add (Ljava/lang/Object;)Z
            POP
         */
        mv.visitVarInsn(AdviceAdapter.ALOAD, parametersIdentifier);
        mv.visitVarInsn(AdviceAdapter.ALOAD, 0);
        mv.visitMethodInsn(
                AdviceAdapter.INVOKEINTERFACE,
                "java/util/List",
                "add",
                "(Ljava/lang/Object;)Z",
                true
        );
        mv.visitInsn(AdviceAdapter.POP);
    }

    /**
     * 获取方法的入参数据，添加到List中
     *
     * @param methodDesc
     * @param mv
     * @param parametersIdentifier
     * @param access
     */
    public static void fillMethodParams(
            String methodDesc,
            MethodVisitor mv,
            int parametersIdentifier,
            int access
    ) {
        LogUtils.println(String.format("%s, fillParameterArray start", TAG));
        /*
            ASM指令：
                ALOAD 5
                ALOAD 4
                INVOKEINTERFACE java/util/List.add (Ljava/lang/Object;)Z
                POP
         */
        // 判断是不是静态函数
        boolean isStatic = (access & 8) != 0;
        // 静态函数与普通函数的cursor不同
        int cursor = isStatic ? 0 : 1;
        Type methodType = Type.getMethodType(methodDesc);
        // 获取参数列表
        Type[] types = methodType.getArgumentTypes();
        int length = types.length;
        LogUtils.println(String.format("%s, fillParameterArray length:%s, types:%s", TAG, length, Arrays.toString(types)));
        for (int i = 0; i < length; ++i) {
            Type it = types[i];
            // 读取列表
            mv.visitVarInsn(AdviceAdapter.ALOAD, parametersIdentifier);
            // 根据不同类型获取不同的指令，比如int是iload, long是lload
            int opcode = it.getOpcode(Opcodes.ILOAD);
            // 通过指令与cursor读取参数的值
            mv.visitVarInsn(opcode, cursor);
            if (it.getSort() >= Type.BOOLEAN && it.getSort() <= Type.DOUBLE) {
                // 基本类型转换为包装类型
                typeCastToObject(mv, it);
            }
            LogUtils.println(String.format("%s, fillParameterArray foreach i:%s, parametersIdentifier:%s, opcode:%s, cursor:%s, it.getSize():%s",
                    TAG, i, parametersIdentifier, opcode, cursor, it.getSize()));
            // 更新cursor
            cursor += it.getSize();
            // 添加到列表中
            mv.visitMethodInsn(
                    AdviceAdapter.INVOKEINTERFACE,
                    "java/util/List",
                    "add",
                    "(Ljava/lang/Object;)Z",
                    true
            );
            mv.visitInsn(AdviceAdapter.POP);
        }
        LogUtils.println(String.format("%s, fillParameterArray end", TAG));
    }

    public static void loadReturnData(MethodVisitor mv, String methodDesc) {
        Type methodType = Type.getMethodType(methodDesc);
        if (methodType.getReturnType().getSize() == 1) {
            mv.visitInsn(AdviceAdapter.DUP);
        } else {
            mv.visitInsn(AdviceAdapter.DUP2);
        }
        typeCastToObject(mv, methodType.getReturnType());
    }

    private static void typeCastToObject(MethodVisitor mv, Type type) {
        if (areEqual(type, Type.INT_TYPE)) {
            mv.visitMethodInsn(AdviceAdapter.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
        } else if (areEqual(type, Type.CHAR_TYPE)) {
            mv.visitMethodInsn(AdviceAdapter.INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
        } else if (areEqual(type, Type.BYTE_TYPE)) {
            mv.visitMethodInsn(AdviceAdapter.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
        } else if (areEqual(type, Type.BOOLEAN_TYPE)) {
            mv.visitMethodInsn(AdviceAdapter.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
        } else if (areEqual(type, Type.SHORT_TYPE)) {
            mv.visitMethodInsn(AdviceAdapter.INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
        } else if (areEqual(type, Type.FLOAT_TYPE)) {
            mv.visitMethodInsn(AdviceAdapter.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
        } else if (areEqual(type, Type.LONG_TYPE)) {
            mv.visitMethodInsn(AdviceAdapter.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
        } else if (areEqual(type, Type.DOUBLE_TYPE)) {
            mv.visitMethodInsn(AdviceAdapter.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
        }
    }

    public static void deliverParams2Dispatcher(
            MethodVisitor mv,
            String className,
            String name,
            int parametersIdentifier
    ) {
        // 对应目标方法的参数
        mv.visitLdcInsn(className); // com/hook/touch/event/MainActivity
        mv.visitLdcInsn(name); // dispatchTouchEvent
        mv.visitVarInsn(AdviceAdapter.ALOAD, parametersIdentifier); // list

        mv.visitMethodInsn(Opcodes.INVOKESTATIC, PluginConst.TARGET_CLASS_PATH, PluginConst.TARGET_METHOD_NAME,
                "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V", false);
    }

    private static boolean areEqual(Object first, Object second) {
        return first == null ? second == null : first.equals(second);
    }

    /**
     * 包名转换
     * // com/hook/touch/event/MainActivity --> com.hook.touch.event.MainActivity
     *
     * @param src
     * @return
     */
    public static String convertPackageName(String src) {
        if (src == null || src.length() == 0) {
            return src;
        }
        return src.replace('/', '.');
    }

    public static void genDispatchTouchEvent(ClassWriter classWriter, ClassInfo classInfo) {
        LogUtils.println(String.format("%s, genDispatchTouchEvent start className:%s, superName:%s", TAG, classInfo.className, classInfo.superName));

        if (classWriter == null || classInfo == null || StringUtils.isEmpty(classInfo.className) || StringUtils.isEmpty(classInfo.superName)) {
            LogUtils.println(String.format("%s, genDispatchTouchEvent failed, cause invalid className or superName", TAG));
            return;
        }

        // 重写dispatchTouchEvent方法
        /*
            {
                mv = cw.visitMethod(ACC_PUBLIC, "dispatchTouchEvent", "(Lcom/tinytongtong/asm/MotionEvent;)Z", null, null);
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitLineNumber(42, l0);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitMethodInsn(INVOKESPECIAL, "com/tinytongtong/asm/BaseDispatcher", "dispatchTouchEvent", "(Lcom/tinytongtong/asm/MotionEvent;)Z", false);
                mv.visitInsn(IRETURN);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLocalVariable("this", "Lcom/tinytongtong/asm/TouchEventDispatcher;", null, l0, l1, 0);
                mv.visitLocalVariable("ev", "Lcom/tinytongtong/asm/MotionEvent;", null, l0, l1, 1);
                mv.visitMaxs(2, 2);
                mv.visitEnd();
            }
         */
        // 正经代码
        MethodVisitor mv = classWriter.visitMethod(
                PluginConst.DispatchMethodInfo.access,
                PluginConst.DispatchMethodInfo.name,
                PluginConst.DispatchMethodInfo.desc,
                PluginConst.DispatchMethodInfo.signature,
                PluginConst.DispatchMethodInfo.exceptions);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
//        mv.visitLineNumber(42, l0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        // superName: androidx/appcompat/app/AppCompatActivity
        mv.visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                classInfo.superName,
                PluginConst.DispatchMethodInfo.name,
                PluginConst.DispatchMethodInfo.desc,
                false
        );
        mv.visitInsn(Opcodes.IRETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        // className: com/hook/touch/event/MainActivity
        mv.visitLocalVariable("this", "L" + classInfo.className + ";", null, l0, l1, 0);
        mv.visitLocalVariable("ev", "Landroid/view/MotionEvent;", null, l0, l1, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();

        LogUtils.println(String.format("%s, genDispatchTouchEvent end", TAG));
    }
}
