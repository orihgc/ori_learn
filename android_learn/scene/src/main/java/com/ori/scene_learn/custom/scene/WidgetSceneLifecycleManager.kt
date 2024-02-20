package com.ori.scene_learn.custom.scene

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
class WidgetSceneLifecycleManager<T : WidgetScene> {

    private lateinit var mWidgetScene: T

    private enum class WidgetSceneLifecycle {
        NONE, ACTIVITY_CREATED, START, RESUME, PAUSE, STOP
    }

    fun onViewCreated(mWidgetScene: WidgetScene) {
        mWidgetScene.onCreate()
    }

}