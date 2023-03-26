package touch.event.gradle.asm.constant;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @Description: constant
 * @Author wangjianzhou
 * @Date 2022/6/15 14:39
 * @Version v0.9.5
 */
public interface PluginConst {
    String TARGET_CLASS_PATH = "touch/event/sdk/TouchEventDispatcher";
    String TARGET_METHOD_NAME = "dispatchTouchEventParams";

    /**
     * Activity#dispatchTouchEvent方法的信息
     */
    interface DispatchMethodInfo {
        /**
         * The method's access flags (see {@link Opcodes}). This field also indicates if the method is
         * synthetic and/or deprecated.
         */
        int access = Opcodes.ACC_PUBLIC;

        /**
         * The method's name.
         */
        String name = "dispatchTouchEvent";

        /**
         * The method's descriptor (see {@link Type}).
         */
        String desc = "(Landroid/view/MotionEvent;)Z";

        /**
         * The method's signature. May be {@literal null}.
         */
        String signature = null;

        /**
         * The internal names of the method's exception classes (see {@link Type#getInternalName()}).
         */
        String[] exceptions = null;
    }

    /**
     * 需要hook的Activity的父类
     */
    interface ActivityParentInfo {
        String v7_AppCompat_Activity = "android/support/v7/app/AppCompatActivity";
        String androidx_AppCompat_Activity = "androidx/appcompat/app/AppCompatActivity";
    }
}
