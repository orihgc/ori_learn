package com.ori.scene_learn.custom.widget

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
class WidgetRecycler {

    companion object {


    }

    val allWidgetContainerList = mutableListOf<WidgetContainer>()
    val mAttachedWidgetContainerList = mutableListOf<WidgetContainer>()
    private val mCachedWidgetContainerList = mutableListOf<WidgetContainer>()
    private val widgetContainerPool = WidgetContainerPool()

}