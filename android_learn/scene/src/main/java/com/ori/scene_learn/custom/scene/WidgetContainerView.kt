package com.ori.scene_learn.custom.scene

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.webkit.WebView

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
class WidgetContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LifecycleFrameLayout(context, attrs, defStyleAttr), IWidgetSceneLifecycle {

    companion object {

    }

    init {
    }


    private lateinit var view: View


    fun bind(data: String) {
        if (data == "web") {
            view = WebView(context)
            addView(view)
        } else {

        }
    }

    private fun sendLifecycleEvent(event: String) {

    }

    override fun onResume() {
        super.onResume()
        if (isAttachedToWindow) {
            onShow()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isAttachedToWindow) {
            onHide()
        }
    }


    override fun onCreate() {
        super.onViewCreated()
        sendLifecycleEvent("onActivityCreated")
    }

    override fun onLoadStart() {
        TODO("Not yet implemented")
    }

    override fun onLoadFinish() {
        TODO("Not yet implemented")
    }

    override fun onRuntimeReady() {
        TODO("Not yet implemented")
    }

    override fun onLoadError() {
        TODO("Not yet implemented")
    }

    override fun onShow() {

    }

    override fun onHide() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        sendLifecycleEvent("onDestroy")
    }

}