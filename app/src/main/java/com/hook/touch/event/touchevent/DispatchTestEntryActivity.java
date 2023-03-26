package com.hook.touch.event.touchevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.aar_module.ImplAARBaseDispatchWithOverrideActivity;
import com.example.aar_module.ImplAARBaseDispatchWithoutOverrideActivity;
import com.example.test_module.ImplModuleBaseDispatchWithOverrideActivity;
import com.example.test_module.ImplModuleBaseDispatchWithoutOverrideActivity;

import touch.event.sdk.util.LogUtils;
import com.hook.touch.event.R;
import com.hook.touch.event.touchevent.base.ImplBaseDispatchWithOverrideActivity;
import com.hook.touch.event.touchevent.base.ImplBaseDispatchWithoutOverrideActivity;
import com.hook.touch.event.touchevent.jar.ImplJarBaseDispatchWithOverrideActivity;
import com.hook.touch.event.touchevent.jar.ImplJarBaseDispatchWithoutOverrideActivity;
import touch.event.sdk.TouchEventManager;

/**
 * @Description: hook dispatchTouchEvent方法的测试页面入口
 * @Author wangjianzhou
 * @Date 2022/6/16 10:00
 * @Version v0.9.5
 */
public class DispatchTestEntryActivity extends AppCompatActivity {
    private static final String TAG = DispatchTestEntryActivity.class.getSimpleName();

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, DispatchTestEntryActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch_test_entry);

        // app
        findViewById(R.id.btn_dispatch_with_override).setOnClickListener(v -> DispatchWithOverrideActivity.actionStart(this));
        findViewById(R.id.btn_dispatch_without_override).setOnClickListener(v -> DispatchWithoutOverrideActivity.actionStart(this));
        findViewById(R.id.btn_dispatch_with_override_kotlin).setOnClickListener(v -> DispatchWithOverrideKotlinActivity.Companion.actionStart(this));
        findViewById(R.id.btn_dispatch_without_override_kotlin).setOnClickListener(v -> DispatchWithoutOverrideKotlinActivity.Companion.actionStart(this));

        // extends base
        findViewById(R.id.btn_dispatch_with_override_base).setOnClickListener(v -> ImplBaseDispatchWithOverrideActivity.actionStart(this));
        findViewById(R.id.btn_dispatch_without_override_base).setOnClickListener(v -> ImplBaseDispatchWithoutOverrideActivity.actionStart(this));

        // module
        findViewById(R.id.btn_dispatch_with_override_base_module).setOnClickListener(v -> ImplModuleBaseDispatchWithOverrideActivity.actionStart(this));
        findViewById(R.id.btn_dispatch_without_override_base_module).setOnClickListener(v -> ImplModuleBaseDispatchWithoutOverrideActivity.actionStart(this));

        // aar
        // ①libs/aar
        // ②module + aar
        findViewById(R.id.btn_dispatch_with_override_base_aar).setOnClickListener(v -> ImplAARBaseDispatchWithOverrideActivity.actionStart(this));
        findViewById(R.id.btn_dispatch_without_override_base_aar).setOnClickListener(v -> ImplAARBaseDispatchWithoutOverrideActivity.actionStart(this));

        // jar
        // ①直接引入jar
        // ②module + jar
        // ③aar + jar
        findViewById(R.id.btn_dispatch_with_override_base_jar).setOnClickListener(v -> ImplJarBaseDispatchWithOverrideActivity.actionStart(this));
        findViewById(R.id.btn_dispatch_without_override_base_jar).setOnClickListener(v -> ImplJarBaseDispatchWithoutOverrideActivity.actionStart(this));

        // long click
        findViewById(R.id.btn_long_click).setOnClickListener(v -> {
            LogUtils.e(TAG, "LongClickTest setOnClickListener#onClick invoked");
        });
        findViewById(R.id.btn_long_click).setOnLongClickListener(v -> {
            LogUtils.e(TAG, "LongClickTest OnLongClickListener#onLongClick invoked");
            return false;
        });

        // 测试-上报所有暂存的事件序列
        findViewById(R.id.btn_dispatch_report_stash_seq).setOnClickListener(v -> TouchEventManager.reportStashSequences());
    }
}