package com.ori.scene_learn.custom.scene

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
class LifecycleFragment : Fragment() {

    private var containerLifecycleCallback: IHostLifecycleCallback? = null

    fun setContainerLifecycleCallback(containerLifecycleCallback: IHostLifecycleCallback) {
        this.containerLifecycleCallback = containerLifecycleCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}