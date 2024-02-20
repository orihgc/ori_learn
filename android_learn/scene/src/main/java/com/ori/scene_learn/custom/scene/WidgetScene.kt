package com.ori.scene_learn.custom.scene

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.ori.scene_learn.custom.widget.WidgetViewModel

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
class WidgetScene : LifecycleOwner, ViewModelStoreOwner {

    private var mView: View? = null

    val widgetViewModel = ViewModelProvider(this).get(WidgetViewModel::class.java)

    private val mLifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle = mLifecycleRegistry
    override fun getViewModelStore(): ViewModelStore = ViewModelStore()

    fun getRealView(): View? = mView

    fun dispatchCreateView(viewGroup: ViewGroup) {
        mView = viewGroup
    }

    fun onCreate() {
        lifecycleScope.launchWhenCreated {

        }
    }

    fun onStart() {
        lifecycleScope.launchWhenStarted {

        }
    }

    fun onResume() {
        lifecycleScope.launchWhenResumed {

        }
    }

    fun onDestroy() {
        mView = null
    }


}