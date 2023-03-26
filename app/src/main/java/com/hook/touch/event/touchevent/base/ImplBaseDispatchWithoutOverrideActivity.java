package com.hook.touch.event.touchevent.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hook.touch.event.R;

/**
 * @Description: 继承自 BaseDispatchActivity ，不复写 dispatchTouchEvent
 * @Author wangjianzhou
 * @Date 2022/6/16 11:28
 * @Version v0.9.5
 */
public class ImplBaseDispatchWithoutOverrideActivity extends BaseDispatchActivity {

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, ImplBaseDispatchWithoutOverrideActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impl_base_dispatch_without_override);
    }
}