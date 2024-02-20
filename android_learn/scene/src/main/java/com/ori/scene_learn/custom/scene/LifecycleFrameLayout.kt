package com.ori.scene_learn.custom.scene

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
open class LifecycleFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var widgetSceneLifecycleManager = WidgetSceneLifecycleManager<WidgetScene>()
    private var mWidgetScene: WidgetScene? = null

    fun setWidgetScene(mWidgetScene: WidgetScene) {
        this.mWidgetScene = mWidgetScene
    }

    open fun onViewCreated() {
        mWidgetScene?.let { widgetSceneLifecycleManager.onViewCreated(it) }
    }

    open fun onStarted() {}
    open fun onResume() {}
    open fun onPause() {}
    open fun onStop() {}

    open fun onHostDestroy() {}

}