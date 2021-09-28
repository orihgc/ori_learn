package com.ori.motion_learn

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.core.view.isVisible

object AnimationUtils{

    fun startTransitionXAnimator(view: View, isPositive: Boolean, translationDistance: Float, setProperty: Animator.()->Unit) {
        val endX = if (isPositive) view.translationX + translationDistance else view.translationX - translationDistance
        val translationYAnimator = ObjectAnimator.ofFloat(view, "translationX", view.translationX, endX)
        val startAlpha = if (isPositive) 0F else 1F
        val endAlpha = if (isPositive) 1F else 0F
        val alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha)
        view.isVisible = true
        translationYAnimator.setProperty()
        alphaAnimator.setProperty()
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationYAnimator, alphaAnimator)
        animatorSet.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                if (!isPositive) view.isVisible = false
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
        animatorSet.start()
    }
}

