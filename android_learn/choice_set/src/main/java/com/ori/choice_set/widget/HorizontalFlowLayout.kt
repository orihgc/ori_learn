package com.ori.choice_set.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

class HorizontalFlowLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0) :
    RelativeLayout(context, attributeSet, defStyle) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // need to call super.onMeasure(...) otherwise we'll get some funny behaviour
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Width and height are initially set to be the requested size by the parent
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        val requiredWidth = measureRequiredWidth(
            paddingLeft,
            paddingRight
        )
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        if (widthMode == MeasureSpec.UNSPECIFIED
            || (widthMode == MeasureSpec.AT_MOST
                    && requiredWidth < width)
        ) {
            // set width as required since there's no height restrictions or if it's less than the maximum allowed
            width = requiredWidth
        }
        val requiredHeight = measureRequiredHeight(
            width,
            paddingTop,
            paddingBottom,
            paddingLeft,
            paddingRight
        )
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == MeasureSpec.UNSPECIFIED
            || (heightMode == MeasureSpec.AT_MOST
                    && requiredHeight < height)
        ) {
            // set height as required since there's no height restriction or if it's less than the maximum allowed
            height = requiredHeight
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // increment the x position (as distance from the START end) as we progress through a line
        var xpos = paddingStart
        // increment the y position as we progress through the lines
        var ypos = paddingTop
        // the height of the current line
        var line_height = 0
        // available width
        val width = r - l

        // note: considering "margins" here...
        // ... but don't need to consider "translations" as translations are done post-layout
        for (i in 0 until childCount) {
            val childWidth: Int
            val childHeight: Int
            val childMarginStart: Int
            val childMarginEnd: Int
            val childMarginTop: Int
            val childMarginBottom: Int
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                childWidth = child.measuredWidth
                childHeight = child.measuredHeight
                if (child.layoutParams != null
                    && child.layoutParams is MarginLayoutParams
                ) {
                    val childMarginLayoutParams = child.layoutParams as MarginLayoutParams
                    childMarginStart = childMarginLayoutParams.marginStart
                    childMarginEnd = childMarginLayoutParams.marginEnd
                    childMarginTop = childMarginLayoutParams.topMargin
                    childMarginBottom = childMarginLayoutParams.bottomMargin
                } else {
                    childMarginStart = 0
                    childMarginEnd = 0
                    childMarginTop = 0
                    childMarginBottom = 0
                }
                if (xpos + childMarginStart + childWidth + childMarginEnd + paddingEnd > width) {
                    // this child will need to go on a new line
                    xpos = paddingStart
                    ypos += line_height
                    line_height = childHeight + childMarginTop + childMarginBottom
                } else {
                    // enough space for this child on the current line
                    line_height = Math.max(
                        line_height,
                        childMarginTop + childHeight + childMarginBottom
                    )
                }
                xpos += childMarginStart

                // Convert from RTL-aware xpos to explicit left/right for child.layout()
                val childLeft = if (layoutDirection == LAYOUT_DIRECTION_LTR) xpos else width - xpos - childWidth
                child.layout(
                    childLeft,
                    ypos + childMarginTop,
                    childLeft + childWidth,
                    ypos + childMarginTop + childHeight
                )
                xpos += childWidth + childMarginEnd
            }
        }
    }

    fun measureRequiredHeight(
        width: Int,
        paddingTop: Int,
        paddingBottom: Int,
        paddingLeft: Int,
        paddingRight: Int
    ): Int {
        // Note: This doesn't need to be RTL-aware, as the required height should be unaffected
        // increment the x position as we progress through a line
        var xpos = paddingLeft
        // increment the y position as we progress through the lines
        var ypos = paddingTop
        // the height of the current line
        var line_height = 0

        // go through children
        // to work out the height required for this view

        // call to measure size of children not needed I think?!
        // getting child's measured height/width seems to work okay without it
        //measureChildren(widthMeasureSpec, heightMeasureSpec);
        var child: View
        var childMarginLayoutParams: MarginLayoutParams
        var childWidth: Int
        var childHeight: Int
        var childMarginLeft: Int
        var childMarginRight: Int
        var childMarginTop: Int
        var childMarginBottom: Int
        for (i in 0 until childCount) {
            child = getChildAt(i)
            if (child.visibility != GONE) {
                childWidth = child.measuredWidth
                childHeight = child.measuredHeight
                if (child.layoutParams != null
                    && child.layoutParams is MarginLayoutParams
                ) {
                    childMarginLayoutParams = child.layoutParams as MarginLayoutParams
                    childMarginLeft = childMarginLayoutParams.leftMargin
                    childMarginRight = childMarginLayoutParams.rightMargin
                    childMarginTop = childMarginLayoutParams.topMargin
                    childMarginBottom = childMarginLayoutParams.bottomMargin
                } else {
                    childMarginLeft = 0
                    childMarginRight = 0
                    childMarginTop = 0
                    childMarginBottom = 0
                }
                if (xpos + childMarginLeft + childWidth + childMarginRight + paddingRight > width) {
                    // this child will need to go on a new line
                    xpos = paddingLeft
                    ypos += line_height
                    line_height = childMarginTop + childHeight + childMarginBottom
                } else {
                    // enough space for this child on the current line
                    line_height = Math.max(
                        line_height,
                        childMarginTop + childHeight + childMarginBottom
                    )
                }
                xpos += childMarginLeft + childWidth + childMarginRight
            }
        }
        ypos += line_height + paddingBottom
        return ypos
    }

    fun measureRequiredWidth(
        paddingLeft: Int,
        paddingRight: Int
    ): Int {
        var requiredWidth = paddingLeft + paddingRight
        var childWidth: Int
        var childMarginLeft: Int
        var childMarginRight: Int
        var child: View
        var childMarginLayoutParams: MarginLayoutParams
        // Go through the children to get the total width of the view
        for (i in 0 until childCount) {
            child = getChildAt(i)
            if (child.visibility != GONE) {
                childWidth = child.measuredWidth
                if (child.layoutParams != null
                    && child.layoutParams is MarginLayoutParams
                ) {
                    childMarginLayoutParams = child.layoutParams as MarginLayoutParams
                    childMarginLeft = childMarginLayoutParams.leftMargin
                    childMarginRight = childMarginLayoutParams.rightMargin
                } else {
                    childMarginLeft = 0
                    childMarginRight = 0
                }
                requiredWidth += childMarginLeft + childWidth + childMarginRight
            }
        }
        return requiredWidth
    }

}