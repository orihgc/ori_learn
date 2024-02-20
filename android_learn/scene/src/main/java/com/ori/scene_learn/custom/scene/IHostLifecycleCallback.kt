package com.ori.scene_learn.custom.scene

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
interface IHostLifecycleCallback {

    fun onHostViewCreated()

    fun onHostStarted()

    fun onHostResumed()

    fun onHostPaused()

    fun onHostStopped()

    fun onHostDestroy()
}