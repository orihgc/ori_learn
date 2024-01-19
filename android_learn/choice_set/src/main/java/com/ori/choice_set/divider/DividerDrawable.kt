package com.ori.choice_set.divider

import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.Drawable

/**
 * @author rexy
 */
class DividerDrawable private constructor(var dividerWidth: Int) {

    var divider: Drawable? = null
    var dividerMarginStart: Int = 0
    var dividerMarginEnd: Int = 0

    val isValidated: Boolean
        get() = dividerWidth != 0 && divider != null

    fun setDividerMargin(margin: Int) {
        dividerMarginStart = margin
        dividerMarginEnd = margin
    }

    fun draw(canvas: Canvas, from: Int, to: Int, middle: Float, horizontal: Boolean) {
        var from = from
        var to = to
        from += dividerMarginStart
        to -= dividerMarginEnd
        if (to > from && dividerWidth > 0 && divider != null) {
            val halfWidth = dividerWidth / 2f
            val middleStart = middle - halfWidth
            val middleEnd = middle + halfWidth
            val start = middleStart.toInt()
            val end = middleEnd.toInt()
            if (horizontal) {
                divider!!.setBounds(from, start, to, end)
            } else {
                divider!!.setBounds(start, from, end, to)
            }
            divider!!.draw(canvas)
        }
    }

    override fun hashCode(): Int {
        var result = if (divider == null) 0 else divider!!.hashCode()
        result = 31 * result + dividerWidth
        result = 31 * result + dividerMarginStart
        result = 31 * result + dividerMarginEnd
        return result
    }

    companion object {

        fun from(attr: TypedArray?, defaultWidth: Int, attrDivider: Int, attrDividerWidth: Int, attrDividerMargin: Int, attrDividerMarginStart: Int, attrDividerMarginEnd: Int): DividerDrawable {
            val dm = DividerDrawable(defaultWidth)
            if (attr != null) {
                if (attr.hasValue(attrDivider)) {
                    dm.divider = attr.getDrawable(attrDivider)
                }
                dm.dividerWidth = attr.getDimensionPixelSize(attrDividerWidth, dm.dividerWidth)
                val hasDefaultMargin = attr.hasValue(attrDividerMargin)
                val defaultMargin = if (hasDefaultMargin) attr.getDimensionPixelSize(attrDividerMargin, 0) else 0
                dm.dividerMarginStart = attr.getDimensionPixelSize(attrDividerMarginStart, if (hasDefaultMargin) defaultMargin else dm.dividerMarginStart)
                dm.dividerMarginEnd = attr.getDimensionPixelSize(attrDividerMarginEnd, if (hasDefaultMargin) defaultMargin else dm.dividerMarginEnd)
            }
            return dm
        }
    }
}