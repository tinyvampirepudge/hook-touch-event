package com.hook.touch.event.touchevent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hook.touch.event.R
import com.hook.touch.event.utils.ReflectUtils

/**
 * @Description: kotlin-不复写dispatchTouchEvent方法
 *
 * @Author wangjianzhou
 * @Date 2022/6/16 10:29
 * @Version v0.9.5
 */
class DispatchWithoutOverrideKotlinActivity : AppCompatActivity() {

    companion object {
        val TAG = DispatchWithoutOverrideKotlinActivity::class.simpleName

        fun actionStart(context: Activity) {
            val starter = Intent(context, DispatchWithoutOverrideKotlinActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatch_without_override_kotlin)

        ReflectUtils.printMethods(javaClass, TAG)
    }
}