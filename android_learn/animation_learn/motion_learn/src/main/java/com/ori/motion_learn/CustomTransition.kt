package com.ori.motion_learn

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.ViewGroup
import android.view.animation.AnimationSet
import androidx.transition.Transition
import androidx.transition.TransitionValues

class CustomTransition : Transition() {
    companion object {
        const val TRANSITION_Y = "transition_y"
        const val ALPHA = "alpha"
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(transitionValues: TransitionValues) {
        val view = transitionValues.view
        val centerY = view.y / 2.625
        transitionValues.values.put(TRANSITION_Y, centerY)
        transitionValues.values.put(ALPHA, view.alpha)
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        val Y = startValues?.values?.get(TRANSITION_Y) as Double
        val alpha = startValues.values?.get(ALPHA) as Float
        val animator =
            ObjectAnimator.ofFloat(sceneRoot, "translationY", (Y.toFloat() + 100F), Y.toFloat())
        val alphaAnimator = ObjectAnimator.ofFloat(sceneRoot, "alpha", alpha, 1F)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animator, alphaAnimator)
        animator.duration = 2000
        return animator
    }
}