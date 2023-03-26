package com.hook.touch.event.touchevent.jar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.example.jar_module.JarBaseActivity;

import com.hook.touch.event.R;

/**
 * @Description: 继承自jar-复写dispatchTouchEvent方法
 *
 * @Author wangjianzhou
 * @Date 2022/6/16 18:34
 * @Version TODO
 */
public class ImplJarBaseDispatchWithOverrideActivity extends JarBaseActivity {

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, ImplJarBaseDispatchWithOverrideActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impl_jar_base_dispatch_with_override);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}