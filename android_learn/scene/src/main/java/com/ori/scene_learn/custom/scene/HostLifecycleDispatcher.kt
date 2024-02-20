package com.ori.scene_learn.custom.scene

import com.ori.scene_learn.custom.widget.WidgetContainerManager

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
object HostLifecycleDispatcher : IHostLifecycleCallback {

    private val allWidgetContainerList get() = WidgetContainerManager.widgetRecycler.allWidgetContainerList

    override fun onHostViewCreated() {
        allWidgetContainerList.forEach {
            it.dispatcher.onHostViewCreated()
        }
    }

    override fun onHostStarted() {
        allWidgetContainerList.forEach {
            it.dispatcher.onHostStarted()
        }
    }

    override fun onHostResumed() {
        allWidgetContainerList.forEach {
            it.dispatcher.onHostResumed()
        }
    }

    override fun onHostPaused() {
        allWidgetContainerList.forEach {
            it.dispatcher.onHostPaused()
        }
    }

    override fun onHostStopped() {
        allWidgetContainerList.forEach {
            it.dispatcher.onHostStopped()
        }
    }

    override fun onHostDestroy() {
        allWidgetContainerList.forEach {
            it.dispatcher.onHostDestroy()
        }
    }
}