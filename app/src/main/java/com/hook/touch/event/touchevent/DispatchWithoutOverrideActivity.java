package com.hook.touch.event.touchevent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.hook.touch.event.R;
import com.hook.touch.event.utils.ReflectUtils;

/**
 * @Description: 不复写dispatchTouchEvent方法
 * {@link android.app.Activity#dispatchTouchEvent(MotionEvent)}
 * 方法
 * @Author wangjianzhou
 * @Date 2022/6/14 18:06
 * @Version v0.9.5
 */
public class DispatchWithoutOverrideActivity extends AppCompatActivity {
    private static final String TAG = "DispatchTouchEventActivity";

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, DispatchWithoutOverrideActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch_without_override);
        ReflectUtils.printMethods(getClass(), TAG);
    }


}