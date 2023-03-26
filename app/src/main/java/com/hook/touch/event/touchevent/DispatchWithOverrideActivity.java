package com.hook.touch.event.touchevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.hook.touch.event.R;
import com.hook.touch.event.utils.ReflectUtils;

/**
 * @Description: 重写了dispatchTouchEvent
 * @Author wangjianzhou
 * @Date 2022/6/16 09:45
 * @Version v0.9.5
 */
public class DispatchWithOverrideActivity extends AppCompatActivity {
    private static final String TAG = DispatchWithOverrideActivity.class.getSimpleName();

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, DispatchWithOverrideActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch_with_override);

        ReflectUtils.printMethods(getClass(), TAG);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}