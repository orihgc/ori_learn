package com.legend.common.utility.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import androidx.dynamicanimation.animation.SpringAnimation
import com.legend.common.utility.animation.config.*
import com.ori.motion_learn.dp2px

class InTurnAnimatorHelper(private val totalInterval: Long, private val isPositive: Boolean) {

    var isFirstLauncher = true

    fun startTranslationWithTransparency(
        view: View,
        configAction: TransitionInTurnAnimationConfig.() -> Unit
    ): Animator {
        val config = TransitionInTurnAnimationConfig()
        config.configAction()
        val startAlpha = if (isFirstLauncher) {
            if (config.needAlphaChange) (if (config.willMoveToNewPosition) 0F else 1F) else 1F
        } else view.alpha

        val endAlpha =
            if (config.needAlphaChange) (if (config.willMoveToNewPosition) 1F else 0F) else 1F
        val endX = when (config.translationDirection) {
            TransitionDirection.RIGHT -> {
                if (config.isLayoutPositionOnLeft) config.translationXDistance else 0F
            }
            TransitionDirection.LEFT -> {
                if (config.isLayoutPositionOnLeft) 0F else -config.translationXDistance
            }
            else -> {
                0F
            }
        }
        val startX = when (config.translationDirection) {
            TransitionDirection.RIGHT -> {
                if (config.isLayoutPositionOnLeft) 0F else -config.translationXDistance
            }
            TransitionDirection.LEFT -> {
                if (config.isLayoutPositionOnLeft) -config.translationXDistance else 0F
            }
            else -> {
                0F
            }
        }
        val endY = when (config.translationDirection) {
            TransitionDirection.BOTTOM -> {
                if (config.isLayoutPositionOnLeft) config.translationYDistance else 0F
            }
            TransitionDirection.TOP -> {
                if (config.isLayoutPositionOnLeft) config.translationYDistance else 0F
            }
            else -> {
                0F
            }
        }
        val startY = when (config.translationDirection) {
            TransitionDirection.BOTTOM -> {
                if (config.isLayoutPositionOnLeft) 0F else -config.translationYDistance
            }
            TransitionDirection.TOP -> {
                if (config.isLayoutPositionOnLeft) -config.translationYDistance else 0F
            }
            else -> {
                0F
            }
        }
        val translationXAnimator = ObjectAnimator.ofFloat(view, "translationX", if (isFirstLauncher) startX else view.translationX, endX)
        val translationYAnimator =
            ObjectAnimator.ofFloat(view, "translationY", startY, endY)
        val alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha)
        translationXAnimator.interpolator = config.interpolator
        translationYAnimator.interpolator = config.interpolator
        val startDelay = if (isPositive) config.startDelay else totalInterval - config.startDelay
        translationXAnimator.startDelay = startDelay
        translationYAnimator.startDelay = startDelay
        alphaAnimator.startDelay = startDelay
        translationXAnimator.duration = config.duration
        translationYAnimator.duration = config.duration
        alphaAnimator.duration = config.duration
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationXAnimator, alphaAnimator, translationYAnimator)
        animatorSet.start()
        return animatorSet
    }

    fun startTranslationXWithDynamicAnimation(
        view: View,
        configAction: DynamicTransitionInTurnAnimationConfig.() -> Unit
    ): SpringAnimation {
        val config = DynamicTransitionInTurnAnimationConfig()
        config.configAction()

        val endX = when (config.translationDirection) {
            TransitionDirection.RIGHT -> {
                0F + config.translationDistance
            }
            else -> 0F
        }
        val transitionXSpringAnimation =
            SpringAnimation(view, SpringAnimation.TRANSLATION_X, endX).apply {
                setStartVelocity(0f)
                spring.stiffness = config.stiffness
                spring.dampingRatio = config.dampingRatio
            }
        if (config.willMoveToNewPosition) {
            transitionXSpringAnimation.setStartValue(view.translationX)
        } else {
            transitionXSpringAnimation.setStartValue(0.dp2px.toFloat())
        }
        return transitionXSpringAnimation
    }


    fun startChangeSize(view: View, configAction: ChangeSizeInTurnAnimationConfig.() -> Unit) {
        val config = ChangeSizeInTurnAnimationConfig()
        config.configAction()
        val targetWidth = if (config.targetWidth != 0) config.targetWidth else view.width
        val targetHeight = if (config.targetHeight != 0) config.targetHeight else view.height
        val widthValueAnimator = ValueAnimator.ofInt(view.width, targetWidth)
        widthValueAnimator.addUpdateListener {
            val layoutParams = view.layoutParams
            layoutParams.width = it.animatedValue as Int
            view.layoutParams = layoutParams
        }
        val heightValueAnimator = ValueAnimator.ofInt(view.height, targetHeight)
        heightValueAnimator.addUpdateListener {
            val layoutParams = view.layoutParams
            layoutParams.height = it.animatedValue as Int
            view.layoutParams = layoutParams
        }
        val startDelay = if (isPositive) config.startDelay else totalInterval - config.startDelay
        widthValueAnimator.startDelay = startDelay
        heightValueAnimator.startDelay = startDelay
        widthValueAnimator.duration = config.duration
        heightValueAnimator.duration = config.duration
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(widthValueAnimator, heightValueAnimator)
        animatorSet.start()
    }

    fun startChangeAlpha(view: View, configAction: AlphaInTurnAnimationConfig.() -> Unit) {
        val config = AlphaInTurnAnimationConfig()
        config.configAction()

        val alphaAnimator = if (config.willShow) ObjectAnimator.ofFloat(
            view,
            "alpha",
            AlphaInTurnAnimationConfig.ZERO_ALPHA,
            AlphaInTurnAnimationConfig.FULL_ALPHA
        ) else ObjectAnimator.ofFloat(
            view,
            "alpha",
            AlphaInTurnAnimationConfig.FULL_ALPHA,
            AlphaInTurnAnimationConfig.ZERO_ALPHA
        )
        val startDelay = if (isPositive) config.startDelay else totalInterval - config.startDelay
        alphaAnimator.startDelay = startDelay
        alphaAnimator.duration = config.duration
        alphaAnimator.start()

    }
}


