package com.ori.scene_learn.custom.widget

import android.content.Context
import com.ori.scene_learn.custom.scene.WidgetContainerView
import com.ori.scene_learn.custom.scene.WidgetScene
import com.ori.scene_learn.custom.scene.WidgetSceneLifecycleDispatcher

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
class WidgetContainer {

    private var state: WidgetState = WidgetState.IDLE

    private enum class WidgetState {
        SHOWING, HIDE, IDLE, CACHED,
    }

    private val widgetScene = WidgetScene()
    val dispatcher = WidgetSceneLifecycleDispatcher()

    fun bindWidget(context: Context, data: String) {
        if (widgetScene.getRealView() == null || state == WidgetState.IDLE || state == WidgetState.CACHED) {
            val containerView = WidgetContainerView(context)
            containerView.bind(data)
            dispatcher.setupWidgetContainerView(containerView)
        } else {
            val widgetContainerView = widgetScene.getRealView() as? WidgetContainerView
            widgetContainerView?.bind(data)
        }
    }


}