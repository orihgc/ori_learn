package com.legend.business.bind.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

const val RADIUS = 14
const val RING_WIDTH = 4

class CustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {


    private val mRingPaint = Paint()

    fun initPaint() {

        mRingPaint.color = Color.parseColor("#D7EBF8")
        mRingPaint.style = Paint.Style.STROKE
        mRingPaint.strokeWidth = RING_WIDTH.dp2px
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(
            RADIUS.dp2px,
            RADIUS.dp2px,
            RADIUS.dp2px,
            mRingPaint
        )
    }

    private inline val Int.dp2px: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        )
}

