package com.ori.motion_learn

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.transition.TransitionValues

class CustomTransition(private val viewGroup: ViewGroup) : Transition() {

    companion object {
        const val ALPHA = "alpha"
        const val TRANSITION_Y = "transition_y"
        const val ALPHA_FULL = 1F
        const val ALPHA_ZERO = 0F
    }

    var transitionY = 100F


    fun beginTransitionY(view: View){
        TransitionManager.beginDelayedTransition(viewGroup, this)
        if (view.alpha == ALPHA_FULL) {
            view.alpha = ALPHA_ZERO
            view.y += transitionY
        } else if (view.alpha == ALPHA_ZERO) {
            view.alpha = ALPHA_FULL
            view.y -= transitionY
        }
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(transitionValues: TransitionValues) {
        transitionValues.values[ALPHA] = transitionValues.view.alpha
        transitionValues.values[TRANSITION_Y] = transitionValues.view.y
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator {
        val startAlpha = startValues?.values?.get(ALPHA) as? Float ?: 0F
        val endAlpha = endValues?.values?.get(ALPHA) as? Float ?: 0F
        val startY = startValues?.values?.get(TRANSITION_Y) as? Float ?: 0F
        val endY = endValues?.values?.get(TRANSITION_Y) as? Float ?: 0F
        val translationAnimator = ObjectAnimator.ofFloat(endValues?.view, "translationY", startY, endY)
        val alphaAnimator = ObjectAnimator.ofFloat(endValues?.view, "alpha", startAlpha, endAlpha)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(alphaAnimator, translationAnimator)
        return animatorSet
    }

}