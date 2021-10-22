package com.legend.common.utility.animation.interpolator

import android.animation.Animator
import android.view.animation.Animation
import android.view.animation.LinearInterpolator

/**
 * 提供了通用的动画插值器扩展，实现缓入缓出、线性变幻等效果，使用方法：
 * animator.easeIn(Speed.FAST)
 *
 * 文档参考：https://bytedance.feishu.cn/wiki/wikcnHDCRClmFbEKWGBIYwFQQZc#
 *
 * @author chenbiao
 * @since 05/14/2021
 */

/**
 * 缓入动画
 */
fun Animator.easeIn(speed: Speed) {
    duration = speed.duration
    interpolator = CommonEaseInInterpolator()
}

/**
 * 缓出动画
 */
fun Animator.easeOut(speed: Speed) {
    duration = speed.duration
    interpolator = CommonEaseOutInterpolator()
}

/**
 * 缓入缓出动画
 */
fun Animator.easeInOut(speed: Speed) {
    duration = speed.duration
    interpolator = CommonEaseInOutInterpolator()
}

/**
 * 线性动画
 */
fun Animator.linear(speed: Speed) {
    duration = speed.duration
    interpolator = LinearInterpolator()
}


fun Animation.easeIn(speed: Speed) {
    duration = speed.duration
    interpolator = CommonEaseInInterpolator()
}

fun Animation.easeOut(speed: Speed) {
    duration = speed.duration
    interpolator = CommonEaseOutInterpolator()
}

fun Animation.easeInOut(speed: Speed) {
    duration = speed.duration
    interpolator = CommonEaseInOutInterpolator()
}

fun Animation.linear(speed: Speed) {
    duration = speed.duration
    interpolator = LinearInterpolator()
}

enum class Speed(val duration: Long) {
    /** 快速动画 160ms */
    FAST(160),
    /** 中速动画 220ms */
    MEDIUM(220),
    /** 慢速动画 280ms */
    SLOW(280)
}