package com.hook.touch.event.touchevent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.hook.touch.event.R
import com.hook.touch.event.utils.ReflectUtils
/**
 * @Description: kotlin-复写dispatchTouchEvent方法
 *
 * @Author wangjianzhou
 * @Date 2022/6/16 10:12
 * @Version v0.9.5
 */
class DispatchWithOverrideKotlinActivity : AppCompatActivity() {

    companion object {
        val TAG = DispatchWithOverrideKotlinActivity::class.simpleName

        fun actionStart(context: Activity) {
            val starter = Intent(context, DispatchWithOverrideKotlinActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatch_with_override_kotlin)
        ReflectUtils.printMethods(javaClass, TAG)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }
}