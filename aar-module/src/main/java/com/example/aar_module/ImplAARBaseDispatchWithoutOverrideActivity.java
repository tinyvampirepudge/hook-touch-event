package com.example.aar_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @Description: aar-继承自base-不复写dispatchTouchEvent
 * @Author wangjianzhou
 * @Date 2022/6/16 17:48
 * @Version TODO
 */
public class ImplAARBaseDispatchWithoutOverrideActivity extends AARBaseActivity {

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, ImplAARBaseDispatchWithoutOverrideActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impl_aarbase_dispatch_without_override);
    }
}