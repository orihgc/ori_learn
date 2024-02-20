package com.ori.assem.base

import android.util.Log
import android.view.View
import com.bytedance.assem.arch.extensions.assemble
import com.bytedance.assem.arch.view.UIContentAssem

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class TaskAddFloatingAssem : UIContentAssem() {

    companion object {
        const val TAG = "TaskAddFloatingAssem"
    }


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        Log.d(TAG, "onViewCreated${view::class.java.name}")
        assemble {

        }


    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}