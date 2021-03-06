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
     * ?????????????????? valueAnimation
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
     * ?????????????????? objectAnimation
     * */
    private fun translationByObjectAnimator(view: ImageView) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationX", -400f, 400f)
        objectAnimator.duration = 1000
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ValueAnimator.REVERSE
        objectAnimator.start()
    }

    /**
     * ?????????????????? propertyValueHolder
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
     * ??????????????????
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
     * ?????????????????? propertyHolder
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
     * ?????????????????? objectAnimator
     * ?????????????????????????????????
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
     * ?????????
     * */
    private fun translationWithInterpolator(view: ImageView) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationX", -400f, 400f)
        objectAnimator.duration = 1000
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ValueAnimator.REVERSE
        //???????????????
        objectAnimator.interpolator = LinearInterpolator()
        //???????????????????????????????????????????????????
        objectAnimator.interpolator = AccelerateInterpolator(3f)
        //???????????????
        objectAnimator.interpolator = DecelerateInterpolator(3f)
        //???????????????????????????
        objectAnimator.interpolator = AccelerateDecelerateInterpolator()
        //?????????????????????2????????????????????????????????????????????????????????????
        objectAnimator.interpolator = AnticipateInterpolator(2f)
        //????????????tension????????????????????????????????????
        objectAnimator.interpolator = AnticipateOvershootInterpolator(2f)
        //???????????????
        objectAnimator.interpolator = CycleInterpolator(0.3f)
        objectAnimator.interpolator = CustomInterpolator()
        //???????????????
        objectAnimator.interpolator = BounceInterpolator()
        objectAnimator.start()
    }

    /**
     * ?????????
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
     * ?????????
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
                // ??????????????????80% ??????????????????
                startValue + (endValue - startValue) / 0.8f * fraction
            }
            fraction <= 0.9 -> {
                // ???????????????80%-90%?????????430???
                endValue + 30 * (fraction - 0.8f) / 0.1f
            }
            else -> {
                // ???????????????90%-100%?????????400???
                endValue + 30 - 30 * (fraction - 0.9f) / 0.1f
            }
        }
    }
}