package com.ori.scene_learn.custom.scene

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
class WidgetSceneLifecycleDispatcher : IWidgetSceneLifecycle, IHostLifecycleCallback {
    private var widgetContainerView: WidgetContainerView? = null

    fun setupWidgetContainerView(widgetContainerView: WidgetContainerView) {
        this.widgetContainerView = widgetContainerView
    }

    override fun onCreate() {
        widgetContainerView?.onCreate()
    }

    override fun onLoadStart() {
        widgetContainerView?.onLoadStart()

    }

    override fun onLoadFinish() {
        widgetContainerView?.onLoadFinish()
    }

    override fun onRuntimeReady() {
        widgetContainerView?.onRuntimeReady()
    }

    override fun onLoadError() {
        widgetContainerView?.onLoadError()
    }

    override fun onShow() {
        widgetContainerView?.onShow()
    }

    override fun onHide() {
        widgetContainerView?.onHide()
    }

    override fun onDestroy() {
        widgetContainerView?.onDestroy()
    }

    override fun onHostViewCreated() {
        widgetContainerView?.onViewCreated()
    }

    override fun onHostStarted() {
        widgetContainerView?.onStarted()
    }

    override fun onHostResumed() {
        widgetContainerView?.onResume()
    }

    override fun onHostPaused() {
        widgetContainerView?.onPause()
    }

    override fun onHostStopped() {
        widgetContainerView?.onStop()
    }

    override fun onHostDestroy() {
        widgetContainerView?.onHostDestroy()
        this.widgetContainerView = null
    }


}