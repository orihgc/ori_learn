package com.legend.common.utility.animation.config

import android.view.animation.PathInterpolator
import com.legend.common.utility.animation.interpolator.CommonEaseInOutInterpolator


class TransitionInTurnAnimationConfig : InTurnAnimationConfig() {
    /**
     * 基于布局位置，是否即将移动到新的位置
     * */
    var willMoveToNewPosition: Boolean = true
    var translationXDistance: Float = 0F
    var translationYDistance: Float = 0F
    var endY: Float = 0F
    var endX: Float = 0F
    var translationDirection: TransitionDirection = TransitionDirection.RIGHT
    var needAlphaChange: Boolean = true
    var interpolator: PathInterpolator = CommonEaseInOutInterpolator()
    var isLayoutPositionOnLeft = false
}

enum class TransitionDirection {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT
}