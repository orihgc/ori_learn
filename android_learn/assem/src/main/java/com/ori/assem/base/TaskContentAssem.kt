package com.ori.assem.base

import android.util.Log
import com.bytedance.assem.arch.extensions.assemViewModel
import com.bytedance.assem.arch.view.UISlotAssem
import com.ori.assem.R

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class TaskContentAssem : UISlotAssem() {

    companion object {
        const val TAG = "TaskContentAssem"
    }

    override fun contentLayoutId(): Int = R.layout.layout_tasks_content

    private val vm: TaskContentVM by assemViewModel()

    override fun onCreate() {
        super.onCreate()
        vm.subscribe { }
        vm.selectSubscribe(TaskContentState::empty) {
            Log.d(TAG, "$it")
        }
        vm.loadTasks()
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