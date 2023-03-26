package touch.event.gradle.asm.visitor;

import touch.event.gradle.asm.utils.PluginUtils;
import touch.event.gradle.asm.utils.LogUtils;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @Description: 处理Activity#dispatchTouchEvent方法
 * @Author wangjianzhou
 * @Date 2022/6/13 18:30
 * @Version v0.9.5
 */
public class TouchEventAdviceAdapter extends AdviceAdapter {
    private static final String TAG = "AdviceAdapter";

    private String className;

    protected TouchEventAdviceAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, String className) {
        super(api, methodVisitor, access, name, descriptor);
        this.className = className;
    }

    @Override
    protected void onMethodEnter() {
        LogUtils.println(String.format("%s, onMethodEnter", TAG));
        // 方法开始
        if (methodDesc != null) {
            // 1、 new一个List，用来放置需要传递的数据
            int parametersIdentifier = PluginUtils.newParameterArrayList(mv, this);
            // 2、 获取调用参数this，添加进list中
            PluginUtils.fillThisParam(mv, parametersIdentifier);
            // 3、 将方法入参填充进列表
            PluginUtils.fillMethodParams(
                    methodDesc, mv, parametersIdentifier, methodAccess
            );
            // 4、 调用帮助类，传递给工具类
            PluginUtils.deliverParams2Dispatcher(mv, className, getName(), parametersIdentifier);
        }
        super.onMethodEnter();
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        LogUtils.println(String.format("%s, onMethodExit", TAG));
    }
}
