package com.ori.choice_set.drawable

import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable

/**
 * 圆形或是椭圆或圆角矩形支持的Drawable
 *
 * @author: rexy
 */
open class OvalRoundRectDrawable(color: Int,
                                 strokeColor: Int = 0,
                                 strokeWidth: Int = 0,
                                 radius: Int = 0,
                                 radiusFraction: Float = 0f,
                                 radiusType: Int = RADIUS_AROUND,
                                 shadowColor: Int = 0,
                                 shadowWidth: Int = 0)
    : Drawable() {

    private val rectF = RectF()
    private val paintRect = RectF()

    private val path by lazy { Path() }

    protected val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var color: Int = 0
        set(value) {
            if (field != value) {
                field = value
                invalidateSelf()
            }
        }
    var strokeColor: Int = 0
        set(value) {
            if (field != value) {
                field = value
                invalidateSelf()
            }
        }
    var strokeWidth
        get() = paint.strokeWidth
        set(value) {
            if (paint.strokeWidth != value) {
                paint.strokeWidth = value
                invalidateSelf()
            }
        }

    var radiusFraction = 0f
        set(value) {
            if (field != value) {
                field = value
                invalidateSelf()
            }
        }

    var radius = 0
        set(value) {
            if (field != value) {
                field = value
                invalidateSelf()
            }
        }

    var radiusType = RADIUS_AROUND
        set(value) {
            val checkValue = if (value == RADIUS_NONE) value else (value and RADIUS_AROUND)
            if (field != checkValue) {
                field = checkValue
                invalidateSelf()
            }
        }

    var shadowColor: Int = 0
        set(value) {
            if (field != value) {
                field = value
                invalidateSelf()
            }
        }

    var shadowWidth: Float = 0f
        set(value) {
            if (field != value) {
                field = value
                blurMaskFilter = null
                invalidateSelf()
            }
        }

    private var blurMaskFilter: BlurMaskFilter? = null

    init {
        this.color = color
        this.strokeColor = strokeColor
        this.strokeWidth = strokeWidth.toFloat()
        this.radius = radius
        this.radiusType = radiusType
        this.radiusFraction = radiusFraction
        this.shadowColor = shadowColor
        this.shadowWidth = shadowWidth.toFloat()
        paint.isAntiAlias = true
    }

    override fun invalidateSelf() {
        if (isVisible && !rectF.isEmpty) {
            super.invalidateSelf()
        }
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        rectF.set(bounds)
    }

    override fun draw(canvas: Canvas) {
        if (rectF.isEmpty) {
            return
        }
        paintRect.set(rectF)
        //暂时画阴影不合预期，先注释掉，有空支持
   /*     getBlurMaskFilter()?.also {
            paint.style = Paint.Style.STROKE
            paint.color = shadowColor
            paint.maskFilter = it
            draw(canvas, false, paintRect)
            paint.maskFilter = null
        }*/
        val halfStroke = strokeWidth / 2f
        if (strokeColor != 0 && strokeWidth > 0) {
            val fillOffset = strokeWidth - 1f
            paint.style = Paint.Style.STROKE
            paintRect.inset(halfStroke, halfStroke)
            prepareDraw(strokeColor, paintRect)
            draw(canvas, false, paintRect)
            paintRect.set(rectF)
            paintRect.inset(fillOffset, fillOffset)
            drawFill(canvas, paintRect)
        } else {
            drawFill(canvas, paintRect)
        }
    }

    open protected fun prepareDraw(color: Int, rect: RectF) {
        paint.color = color
    }

    private fun drawFill(canvas: Canvas, rect: RectF) {
        if (color != 0) {
            paint.style = Paint.Style.FILL
            prepareDraw(color, rect)
            draw(canvas, true, rect)
        }
    }

    private fun draw(canvas: Canvas, fillType: Boolean, rect: RectF) {
        if (radius > 0f || radiusFraction in 0.0f..0.5f) {
            val r = if (RADIUS_NONE == radiusType) {
                0.0f
            } else {
                if (radiusFraction > 0) rect.height().coerceAtMost(rect.width()) * radiusFraction else radius.toFloat()
            }
            if (r == 0.0f) {
                canvas.drawRect(rect, paint)
            } else {
                if (RADIUS_AROUND == radiusType) {
                    canvas.drawRoundRect(rect, r, r, paint)
                } else {
                    drawPath(canvas, rect, r, radiusType, fillType)
                }
            }
        } else {
            canvas.drawArc(rect, 0f, 360f, fillType, paint)
        }
    }

    private fun drawPath(canvas: Canvas, rect: RectF, radius: Float, f: Int, fillType: Boolean) {
        path.reset()

        val r1 = if (RADIUS_LEFT_TOP == (RADIUS_LEFT_TOP and f)) radius else 0f
        val r2 = if (RADIUS_RIGHT_TOP == (RADIUS_RIGHT_TOP and f)) radius else 0f
        val r3 = if (RADIUS_RIGHT_BOTTOM == (RADIUS_RIGHT_BOTTOM and f)) radius else 0f
        val r4 = if (RADIUS_LEFT_BOTTOM == (RADIUS_LEFT_BOTTOM and f)) radius else 0f

        val l = rect.left
        val t = rect.top
        val r = rect.right
        val b = rect.bottom

        val r1x = l + r1
        val r1y = t + r1

        val r2x = r - r2
        val r2y = t + r2

        val r3x = r - r3
        val r3y = b - r3

        val r4x = l + r4
        val r4y = b - r4

        if (r1 == 0f) {
            path.moveTo(r1x, r1y)
        }
        arcToLine(rect, r1, 180f, r1x, r1y, r2x, t)
        arcToLine(rect, r2, 270f, r2x, r2y, r, r3y)
        arcToLine(rect, r3, 0f, r3x, r3y, r4x, b)
        arcToLine(rect, r4, 90f, r4x, r4y, l, r1y)
        if (!fillType) {
            path.close()
        }
        canvas.drawPath(path, paint)
    }

    private fun arcToLine(rect: RectF, r: Float, startAngle: Float, rx: Float, ry: Float, x: Float, y: Float) {
        if (r != 0f) {
            rect.left = rx - r
            rect.right = rx + r
            rect.top = ry - r
            rect.bottom = ry + r
            path.arcTo(rect, startAngle, 90f, false)
        }
        path.lineTo(x, y)
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    protected fun getBlurMaskFilter(): BlurMaskFilter? {
        if (shadowColor == 0 || shadowWidth <= 0) {
            return null
        }
        if (blurMaskFilter == null) {
            blurMaskFilter = BlurMaskFilter(shadowWidth, BlurMaskFilter.Blur.INNER)
        }
        return blurMaskFilter
    }

    companion object {
        const val RADIUS_NONE = 0
        const val RADIUS_LEFT_TOP = 1
        const val RADIUS_LEFT_BOTTOM = 2
        const val RADIUS_RIGHT_TOP = 4
        const val RADIUS_RIGHT_BOTTOM = 8
        const val RADIUS_AROUND = RADIUS_LEFT_TOP or RADIUS_LEFT_BOTTOM or RADIUS_RIGHT_TOP or RADIUS_RIGHT_BOTTOM
        const val RADIUS_LEFT = RADIUS_LEFT_TOP or RADIUS_LEFT_BOTTOM
        const val RADIUS_TOP = RADIUS_LEFT_TOP or RADIUS_RIGHT_TOP
        const val RADIUS_RIGHT = RADIUS_RIGHT_TOP or RADIUS_RIGHT_BOTTOM
        const val RADIUS_BOTTOM = RADIUS_LEFT_BOTTOM or RADIUS_RIGHT_BOTTOM
    }
}