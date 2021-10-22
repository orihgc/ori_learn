package com.legend.common.utility.animation.config

class DynamicTransitionInTurnAnimationConfig : InTurnAnimationConfig() {
    var willMoveToNewPosition: Boolean = true
    var translationDistance: Float = 0F
    var translationDirection: TransitionDirection = TransitionDirection.RIGHT
    var needAlphaChange: Boolean = true
    var stiffness: Float = 0f
    var dampingRatio: Float = 0f
}