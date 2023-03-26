package com.example.test_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * @Description: @Description: module-继承自base-复写dispatchTouchEvent
 * @Author tinytongtong
 * @Date 2022/6/16 15:51
 * @Version TODO
 */
public class ImplModuleBaseDispatchWithOverrideActivity extends ModuleBaseActivity {

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, ImplModuleBaseDispatchWithOverrideActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impl_module_base_dispatch_with_override);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}