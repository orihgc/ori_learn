package com.ori.scene_learn.custom.scene

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
interface IWidgetSceneLifecycle {

    fun onCreate()

    fun onLoadStart()

    fun onLoadFinish()

    fun onRuntimeReady()

    fun onLoadError()

    fun onShow()

    fun onHide()

    fun onDestroy()
}