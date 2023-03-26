package com.hook.touch.event.touchevent.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.hook.touch.event.R;

/**
 * @Description: 继承自 BaseDispatchActivity ，复写 dispatchTouchEvent
 * @Author wangjianzhou
 * @Date 2022/6/16 11:29
 * @Version V0.9.5
 */
public class ImplBaseDispatchWithOverrideActivity extends BaseDispatchActivity {

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, ImplBaseDispatchWithOverrideActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impl_base_dispatch_with_override);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        System.out.println(ev);
        return super.dispatchTouchEvent(ev);
    }
}