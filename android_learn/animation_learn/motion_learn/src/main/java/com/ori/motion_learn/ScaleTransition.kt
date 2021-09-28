package com.ori.motion_learn

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionValues

class ScaleTransition: Transition() {
    companion object {
        const val WIDTH = "width"
        const val ALPHA_FULL = 1F
        const val ALPHA_ZERO = 0F
    }


    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(transitionValues: TransitionValues) {
        transitionValues.values[WIDTH] = transitionValues.view.layoutParams.width.toFloat()
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        val startWidth = startValues?.values?.get(WIDTH) as Float
        val endWidth = endValues?.values?.get(WIDTH) as Float
        return ObjectAnimator.ofFloat(sceneRoot, "scaleX", startWidth / endWidth, 1F)
    }
}