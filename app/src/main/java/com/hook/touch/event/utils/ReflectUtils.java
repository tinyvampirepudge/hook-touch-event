package com.hook.touch.event.utils;

import touch.event.sdk.util.LogUtils;

import java.lang.reflect.Method;

/**
 * @Description: 反射查看class信息
 * @Author wangjianzhou
 * @Date 2022/6/16 10:34
 * @Version v0.9.5
 */
public class ReflectUtils {

    /**
     * 通过反射，查看通过ASM添加的方法是否成功
     */
    public static void printMethods(Class clazz, String TAG) {
        LogUtils.e(TAG, "printMethods start");
        // 获取所有的 public 修饰的方法，包括父类的。
        Method[] methods = clazz.getMethods();
        // 获取声明是所有本类的方法（不包括父类的）
        Method[] declaredMethods = clazz.getDeclaredMethods();
        methods = declaredMethods;
        for (int i = 0; i < methods.length; i++) {
            LogUtils.e(TAG, "printMethods ----------第" + (i + 1) + "个方法----------");
            Method method = methods[i];
            // 获取方法的返回值类类型
            Class<?> returnType = method.getReturnType();
            LogUtils.e(TAG, "printMethods 获取方法的返回值类类型:" + returnType.getName());
            // 获取方法的名称
            LogUtils.e(TAG, "printMethods 获取方法的名称：" + method.getName());
            // 获取参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length > 0) {
                LogUtils.e(TAG, "printMethods 获取所有参数如下：");
                for (int i1 = 0; i1 < parameterTypes.length; i1++) {
                    LogUtils.e(TAG, "printMethods   第" + (i1 + 1) + "个参数：" + parameterTypes[i1]);
                }
            }
        }
        LogUtils.e(TAG, "printMethods start");
    }
}
