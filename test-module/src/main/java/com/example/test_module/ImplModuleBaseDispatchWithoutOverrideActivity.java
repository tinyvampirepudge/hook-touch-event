package com.example.test_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @Description: module-继承自base-不复写dispatchTouchEvent
 *
 * @Author wangjianzhou
 * @Date 2022/6/16 15:50
 * @Version TODO
 */
public class ImplModuleBaseDispatchWithoutOverrideActivity extends ModuleBaseActivity {

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, ImplModuleBaseDispatchWithoutOverrideActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impl_module_base_dispatch_without_override);
    }
}