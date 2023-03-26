package com.hook.touch.event;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * @Description: Application入口
 *
 * @Author wangjianzhou
 * @Date 2023/3/26 16:00
 * @Version
 */
public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //运行时多Dex加载， 继承MultiDexApplication最终也是调用这个方法
        MultiDex.install(this);
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onLowMemory() {
        super.onLowMemory();
    }
}
