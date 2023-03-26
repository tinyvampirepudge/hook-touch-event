package com.hook.touch.event;

import android.graphics.Outline;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.appcompat.app.AppCompatActivity;

import touch.event.sdk.util.LogUtils;

import com.hook.touch.event.touchevent.DispatchTestEntryActivity;

/**
 * @Description: 主界面入口
 * @Author wangjianzhou
 * @Date 2023/3/26 16:00
 * @Version TODO
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = findViewById(R.id.view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                LogUtils.d(TAG, "old touch :action = " + motionEvent.getAction()
                        + "，\n getX = " + motionEvent.getX() + ", \n getY = " + motionEvent.getY());

                return false;
            }
        });

        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20);
            }
        });
        view.setClipToOutline(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d(TAG, "old view click------");
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LogUtils.d(TAG, "old view onLongClick------");
                return false;
            }
        });

        findViewById(R.id.btn_dispatch_test).setOnClickListener(v -> DispatchTestEntryActivity.actionStart(this));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}