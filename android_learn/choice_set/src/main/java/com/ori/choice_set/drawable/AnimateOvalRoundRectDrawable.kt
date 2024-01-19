package com.ori.choice_set.drawable

import android.graphics.RectF
import android.graphics.drawable.Animatable
import android.os.SystemClock
import android.util.Log

/**
 * @author: rexy
 */
class AnimateOvalRoundRectDrawable(color: Int, strokeColor: Int = 0, strokeWidth: Int = 0, radius: Int = 0, radiusFraction: Float = 0f, radiusType: Int = RADIUS_AROUND, shadowColor:Int=0, shadowWidth:Int=0) : OvalRoundRectDrawable(color, strokeColor, strokeWidth, radius, radiusFraction, radiusType,shadowColor,shadowWidth), Runnable, Animatable {

    private var currentAlphaFraction = 0f
    private var animStartTime: Long = 0
    private var devDebug = false
    private var running = false
    private var animating = false

    private var fromAlphaFraction = 0f
    private var toAlphaFraction = 0.06f
    private var duration = 120

    private var animToVisible = false
    private var alphaFractionMin = fromAlphaFraction.coerceAtLeast(toAlphaFraction)
    private var alphaFractionMax = fromAlphaFraction.coerceAtMost(toAlphaFraction)

    var animEndCallback:((toVisible:Boolean)->Unit)?=null

    private fun print(msg: String) {
        Log.d("AnimateDrawable", "$msg")
    }

    fun alphaFraction(fromFraction: Float, toFraction: Float): AnimateOvalRoundRectDrawable {
        var fromAlpha = fromFraction
        var toAlpha = toFraction
        if (fromAlpha < 0 || fromAlpha > 1f) {
            fromAlpha = fromAlphaFraction
        }
        if (toAlpha < 0 || toAlpha > 1f) {
            toAlpha = toAlphaFraction
        }
        if (fromAlpha != fromAlphaFraction || toAlpha != toAlphaFraction) {
            fromAlphaFraction = fromAlpha
            toAlphaFraction = toAlpha
            alphaFractionMin = fromAlphaFraction.coerceAtLeast(toAlphaFraction)
            alphaFractionMax = fromAlphaFraction.coerceAtMost(toAlphaFraction)
            currentAlphaFraction = if (running) {
                val percent = ((SystemClock.uptimeMillis() - animStartTime) / duration.toFloat().coerceAtLeast(0f)).coerceAtMost(1f)
                val startAlpha = if (animToVisible) this.fromAlphaFraction else toAlphaFraction
                startAlpha + (currentAlphaFraction - startAlpha) * percent
            } else {
                if (animToVisible) toAlphaFraction else fromAlphaFraction
            }
            invalidateSelf()
        }
        return this@AnimateOvalRoundRectDrawable
    }

    fun duration(duration: Int): AnimateOvalRoundRectDrawable {
        if (duration > 0) {
            val delta = this.duration - duration
            if (running) {
                animStartTime += delta.toLong()
            }
            this.duration = duration
        }
        return this@AnimateOvalRoundRectDrawable
    }

    override fun setVisible(visible: Boolean, restart: Boolean): Boolean {
        val changed = super.setVisible(visible, restart)
        if (devDebug) {
            print(String.format("setVisible(visible=%s,restart=%s)", visible, restart))
        }
        if (visible) {
            if (restart || changed) {
                var targetAlpha = currentAlphaFraction
                if (animating) {
                    if (restart) {
                        animStartTime = SystemClock.uptimeMillis()
                        targetAlpha = if (animToVisible) fromAlphaFraction else toAlphaFraction
                    } else {
                        animStartTime = calculateStartTime(currentAlphaFraction, animToVisible)
                    }
                }
                setFrame(targetAlpha, true, toAlphaFraction != fromAlphaFraction && animating)
            }
        } else {
            unscheduleSelf(this)
        }
        return changed
    }


    fun start(toVisible: Boolean) {
        if (duration <= 0 || toAlphaFraction == fromAlphaFraction) return
        animating = true
        if (running) {
            if (animToVisible != toVisible) {
                animToVisible = toVisible
                animStartTime = calculateStartTime(currentAlphaFraction, toVisible)
            }
        } else {
            animToVisible = toVisible
            var targetFrame = currentAlphaFraction
            if (targetFrame < alphaFractionMin || targetFrame > alphaFractionMax) {
                targetFrame = if (animToVisible) fromAlphaFraction else toAlphaFraction
            }
            animStartTime = calculateStartTime(targetFrame, toVisible)
            if (devDebug) {
                print(String.format("start(toVisible=%s)", animToVisible))
            }
            setFrame(targetFrame, false, toAlphaFraction != fromAlphaFraction)
        }
    }

    override fun start() {
        start(if (running) animToVisible else !animToVisible)
    }

    override fun stop() {
        animating = false
        if (isRunning) {
            if (devDebug) {
                print(String.format("stop(lastVisible=%s)", animToVisible))
            }
            unscheduleSelf(this)
        }
    }

    override fun isRunning(): Boolean {
        return running
    }

    override fun run() {
        val percent = (SystemClock.uptimeMillis() - animStartTime) / duration.toFloat()
        val animFinished = percent > 1f || percent < 0
        val endAlpha = if (animToVisible) toAlphaFraction else fromAlphaFraction
        if (animFinished) {
            if (devDebug) {
                print(String.format("run-end(toVisible=%s,target=%s)", animToVisible, endAlpha))
            }
            setFrame(endAlpha, false, false)
            stop()
            animEndCallback?.invoke(animToVisible)
        } else {
            val startAlpha = if (animToVisible) fromAlphaFraction else toAlphaFraction
            val targetAlpha = startAlpha + (endAlpha - startAlpha) * percent
            if (devDebug) {
                print(String.format("run-ing(toVisible=%s,percent=%3f,target=%s)", animToVisible, percent, targetAlpha))
            }
            setFrame(targetAlpha, false, true)
        }
    }

    private fun setFrame(frame: Float, unschedule: Boolean, animate: Boolean) {
        var frame = frame
        if (animate) {
            if (frame < alphaFractionMin) {
                frame = alphaFractionMin
            }
            if (frame > alphaFractionMax) {
                frame = alphaFractionMax
            }
        }
        animating = animate
        if (frame != currentAlphaFraction) {
            currentAlphaFraction = frame
            invalidateSelf()
        }
        if (unschedule) {
            unscheduleSelf(this)
        }
        if (animate) {
            running = true
            scheduleSelf(this, SystemClock.uptimeMillis() + 16)
        }
    }

    override fun prepareDraw(color: Int, rect: RectF) {
        super.prepareDraw(color and 0x00FFFFFF or ((currentAlphaFraction * 255).toInt() shl 24), rect)
    }

    override fun unscheduleSelf(what: Runnable) {
        if (what === this) {
            running = false
        }
        super.unscheduleSelf(what)
    }

    private fun calculateStartTime(current: Float, toVisible: Boolean): Long {
        val finished = (if (toVisible) current - fromAlphaFraction else toAlphaFraction - current) / (toAlphaFraction - fromAlphaFraction)
        var added = finished.coerceAtLeast(0f).coerceAtMost(1f)
        return SystemClock.uptimeMillis() + (added * duration).toLong()
    }
}
