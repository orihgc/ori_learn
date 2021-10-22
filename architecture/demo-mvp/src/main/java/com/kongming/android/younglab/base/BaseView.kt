package com.kongming.android.younglab.base

import androidx.annotation.StringRes

/**
 * MVP模式Base View接口。
 * View实现UI逻辑，定义基本的UI方法。
 */
interface BaseView {

    /**
     * 打印Toast
     */
    fun showToast(msg: String?)

    /**
     * 打印Toast
     */
    fun showToast(@StringRes id: Int)

    /**
     * 显示加载中dialog
     *
     * @param title
     */
    fun showLoadingDialog(title: String)

    /**
     * 隐藏加载中dialog
     */
    fun hideLoadingDialog()
}