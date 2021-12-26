package com.ori.animation_learn

import android.animation.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.animation.addListener
import kotlinx.android.synthetic.main.activity_property_animation.*

class PropertyAnimationActivity : AppCompatActivity() {

    var isVisibleTest = true


    companion object {
        private const val ANIMATION_DURATION = 4000L
        private const val ZERO_FLOAT = 0F
        private const val ONE_FLOAT = 1F
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_animation)
        btn.setOnClickListener {
            view_test.backgroundTintList= ColorStateList.valueOf(Color.RED)
        }
    }


    private var animatorSet: AnimatorSet = AnimatorSet()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun scale(view: View, isEnlarge: Boolean) {
        val startValue = if (isEnlarge) ZERO_FLOAT else ONE_FLOAT
        val endValue = if (isEnlarge) ONE_FLOAT else ZERO_FLOAT
        val xScaleAnimator =
            ObjectAnimator.ofFloat(view, "scaleX", startValue, endValue)
        val yScaleAnimator =
            ObjectAnimator.ofFloat(view, "scaleY", startValue, endValue)
        val alphaScaleAnimator =
            ObjectAnimator.ofFloat(view, "alpha", startValue, endValue)
        view.pivotX = view.width.div(2).toFloat()
        view.pivotY = 0f
        xScaleAnimator.duration = ANIMATION_DURATION
        yScaleAnimator.duration = ANIMATION_DURATION
        alphaScaleAnimator.duration = ANIMATION_DURATION
        val pathInterpolator = PathInterpolator(0.3F, 1.3F, 0.3F, 1F)
        xScaleAnimator.interpolator = pathInterpolator
        yScaleAnimator.interpolator = pathInterpolator
        alphaScaleAnimator.interpolator = pathInterpolator
        animatorSet.playTogether(xScaleAnimator, yScaleAnimator, alphaScaleAnimator)
        animatorSet.start()
    }

    /**
     * 平移动画实现 valueAnimation
     * */
    private fun translationByValueAnimator(view: View) {
        val valueAnimator = ValueAnimator.ofFloat(-400f, 400f)
        valueAnimator.addUpdateListener { animation ->
            view.translationX = animation?.animatedValue as Float
        }
        valueAnimator.duration = 1000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.start()
    }

    /**
     * 平移动画实现 objectAnimation
     * */
    private fun translationByObjectAnimator(view: ImageView) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationX", -400f, 400f)
        objectAnimator.duration = 1000
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ValueAnimator.REVERSE
        objectAnimator.start()
    }

    /**
     * 平移动画实现 propertyValueHolder
     * */
    private fun translationByPropertyValueHolder(view: ImageView) {
        val propertyValuesHolder = PropertyValuesHolder.ofFloat("translationX", -400f, 400f)
        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, propertyValuesHolder)
        objectAnimator.duration = 1000
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ValueAnimator.REVERSE
        objectAnimator.start()
    }

    /**
     * 动画组合效果
     * */
    private fun combineAnimationSet(view: ImageView) {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.addUpdateListener { animation ->
            view.alpha = (animation.animatedValue) as Float
            view.translationX = 400 * ((animation?.animatedValue as Float) * 2 - 1)
        }
        valueAnimator.duration = 1000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.start()
    }

    /**
     * 动画组合效果 propertyHolder
     * */
    private fun combineAnimationSetByPropertyHolder(view: ImageView) {
        val alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        val translationHolder = PropertyValuesHolder.ofFloat("translationX", -400f, 400f)
        val objectAnimator =
            ObjectAnimator.ofPropertyValuesHolder(view, alphaHolder, translationHolder)
        objectAnimator.duration = 1000
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ValueAnimator.REVERSE
        objectAnimator.start()
    }

    /**
     * 动画组合效果 objectAnimator
     * 平移、缩放、选择、渐变
     * */
    private fun combineAnimationByObject(view: ImageView) {
        val alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        alphaAnimator.duration = 1000
        alphaAnimator.repeatCount = ValueAnimator.INFINITE
        alphaAnimator.repeatMode = ValueAnimator.REVERSE
        val translationAnimator = ObjectAnimator.ofFloat(view, "translationX", -400f, 400f)
        translationAnimator.duration = 2000
        translationAnimator.repeatCount = ValueAnimator.INFINITE
        translationAnimator.repeatMode = ValueAnimator.REVERSE
        val rotationAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        rotationAnimator.duration = 1000
        rotationAnimator.repeatCount = ValueAnimator.INFINITE
        rotationAnimator.repeatMode = ValueAnimator.RESTART
        val scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
        scaleXAnimator.duration = 1000
        scaleXAnimator.repeatCount = ValueAnimator.INFINITE
        scaleXAnimator.repeatMode = ValueAnimator.REVERSE
        val scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
        scaleYAnimator.duration = 1000
        scaleYAnimator.repeatCount = ValueAnimator.INFINITE
        scaleYAnimator.repeatMode = ValueAnimator.REVERSE
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            alphaAnimator,
            translationAnimator,
            rotationAnimator,
            scaleXAnimator,
            scaleYAnimator
        )
        animatorSet.start()
    }

    /**
     * 差值器
     * */
    private fun translationWithInterpolator(view: ImageView) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationX", -400f, 400f)
        objectAnimator.duration = 1000
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ValueAnimator.REVERSE
        //线性差值器
        objectAnimator.interpolator = LinearInterpolator()
        //加速差值器，参数越大，速度越来越快
        objectAnimator.interpolator = AccelerateInterpolator(3f)
        //减速差值器
        objectAnimator.interpolator = DecelerateInterpolator(3f)
        //先加速后减速差值器
        objectAnimator.interpolator = AccelerateDecelerateInterpolator()
        //张力值，默认为2，参数越大，初始的偏移值越大，且速度越快
        objectAnimator.interpolator = AnticipateInterpolator(2f)
        //张力值，tension，起始和结束时的偏移越大
        objectAnimator.interpolator = AnticipateOvershootInterpolator(2f)
        //周期差值器
        objectAnimator.interpolator = CycleInterpolator(0.3f)
        objectAnimator.interpolator = CustomInterpolator()
        //弹跳差值器
        objectAnimator.interpolator = BounceInterpolator()
        objectAnimator.start()
    }

    /**
     * 估值器
     * */
    private fun startObjectAnimatorAnim(view: ImageView) {
        val valueAnimator = ValueAnimator.ofObject(CustomEvaluator(), -400f, 400f)
        valueAnimator.interpolator = CustomInterpolator()
        valueAnimator.addUpdateListener { animation ->
            view.translationX = animation.animatedValue as Float
        }
        valueAnimator.duration = 1000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.start()
    }

    /**
     * 关键帧
     * */
    private fun startObjectAnimatorAnimFrame(view: ImageView) {
        val alphaHolder = PropertyValuesHolder.ofFloat("alpha", 1f, 1f, 0f)
        val translationHolder = PropertyValuesHolder.ofFloat("translationX", -400f, 400f)
        val animator: ObjectAnimator =
            ObjectAnimator.ofPropertyValuesHolder(view, alphaHolder, translationHolder)
        animator.duration = 1000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.start()
    }
}

class CustomInterpolator : TimeInterpolator {
    override fun getInterpolation(input: Float): Float {
        if (input < 0.5f) {
            return input / 2
        }
        return input * input
    }
}

class CustomEvaluator : TypeEvaluator<Float> {
    override fun evaluate(fraction: Float, startValue: Float, endValue: Float): Float {
        return when {
            fraction <= 0.8 -> {
                // 动画执行的前80% 走完全部路径
                startValue + (endValue - startValue) / 0.8f * fraction
            }
            fraction <= 0.9 -> {
                // 动画执行的80%-90%时走到430处
                endValue + 30 * (fraction - 0.8f) / 0.1f
            }
            else -> {
                // 动画执行的90%-100%时返回400处
                endValue + 30 - 30 * (fraction - 0.9f) / 0.1f
            }
        }
    }
}