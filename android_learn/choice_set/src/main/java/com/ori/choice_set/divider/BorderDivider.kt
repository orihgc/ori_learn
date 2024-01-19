package com.ori.choice_set.divider

import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import com.ori.choice_set.R

/**
 * 描述 Divider 和 margin 的信息类,可独立画divider。支持纯色divider或是 Drawable。
 * 具体属性见
 *
 * <attr name="borderLeft" format="reference|color"></attr>
 * <attr name="borderLeftWidth" format="dimension"></attr>
 * <attr name="borderLeftMargin" format="dimension"></attr>
 * <attr name="borderLeftMarginStart" format="dimension"></attr>
 * <attr name="borderLeftMarginEnd" format="dimension"></attr>
 *
 *
 *
 * <attr name="borderTop" format="reference|color"></attr>
 * <attr name="borderTopWidth" format="dimension"></attr>
 * <attr name="borderTopMargin" format="dimension"></attr>
 * <attr name="borderTopMarginStart" format="dimension"></attr>
 * <attr name="borderTopMarginEnd" format="dimension"></attr>
 *
 *
 *
 * <attr name="borderRight" format="reference|color"></attr>
 * <attr name="borderRightWidth" format="dimension"></attr>
 * <attr name="borderRightMargin" format="dimension"></attr>
 * <attr name="borderRightMarginStart" format="dimension"></attr>
 * <attr name="borderRightMarginEnd" format="dimension"></attr>
 *
 *
 *
 * <attr name="borderBottom" format="reference|color"></attr>
 * <attr name="borderBottomWidth" format="dimension"></attr>
 * <attr name="borderBottomMargin" format="dimension"></attr>
 * <attr name="borderBottomMarginStart" format="dimension"></attr>
 * <attr name="borderBottomMarginEnd" format="dimension"></attr>
 *
 *
 *
 * <attr name="contentMarginHorizontal" format="dimension"></attr>
 * <attr name="contentMarginVertical" format="dimension"></attr>
 * <attr name="contentMargin" format="dimension"></attr>
 * <attr name="contentMarginLeft" format="dimension"></attr>
 * <attr name="contentMarginTop" format="dimension"></attr>
 * <attr name="contentMarginRight" format="dimension"></attr>
 * <attr name="contentMarginBottom" format="dimension"></attr>
 *
 * <attr name="itemMarginBetween" format="dimension"></attr>
 * <attr name="itemMarginHorizontal" format="dimension"></attr>
 * <attr name="itemMarginVertical" format="dimension"></attr>
 *
 *
 *
 *
 *
 * <attr name="dividerDrawableHorizontal" format="reference|color"></attr>
 *
 * <attr name="dividerWidthHorizontal" format="dimension"></attr>
 *
 * <attr name="dividerPaddingHorizontal" format="dimension"></attr>
 * <attr name="dividerPaddingHorizontalStart" format="dimension"></attr>
 * <attr name="dividerPaddingHorizontalEnd" format="dimension"></attr>
 *
 *
 *
 * <attr name="dividerDrawableVertical" format="reference|color"></attr>
 *
 * <attr name="dividerWidthVertical" format="dimension"></attr>
 *
 * <attr name="dividerPaddingVertical" format="dimension"></attr>
 * <attr name="dividerPaddingVerticalStart" format="dimension"></attr>
 * <attr name="dividerPaddingVerticalEnd" format="dimension"></attr>
 *
 * @author: rexy
 */
class BorderDivider private constructor(attr: TypedArray?, dividerWidthDefault: Int) {
    var contentMargin = Rect()
        internal set
    var itemMarginHorizontal: Int = 0
    var itemMarginVertical: Int = 0
    var borderLeft: DividerDrawable
        internal set
    var borderTop: DividerDrawable
        internal set
    var borderRight: DividerDrawable
        internal set
    var borderBottom: DividerDrawable
        internal set

    var dividerHorizontal: DividerDrawable
        internal set
    var dividerVertical: DividerDrawable
        internal set

    val isVisibleDividerHorizontal: Boolean
        get() = dividerHorizontal.isValidated

    val isVisibleDividerVertical: Boolean
        get() = dividerVertical.isValidated

    val isVisibleBorder: Boolean
        get() = borderLeft.isValidated ||
                borderTop.isValidated ||
                borderRight.isValidated ||
                borderBottom.isValidated

    init {
        val widthDefault = attr?.getDimensionPixelSize(R.styleable.WidgetLayout_defaultBorderDividerWidth, dividerWidthDefault)
                ?: dividerWidthDefault
        borderLeft = DividerDrawable.from(
            attr, widthDefault,
            R.styleable.WidgetLayout_borderLeft,
            R.styleable.WidgetLayout_borderLeftWidth,
            R.styleable.WidgetLayout_borderLeftMargin,
            R.styleable.WidgetLayout_borderLeftMarginStart,
            R.styleable.WidgetLayout_borderLeftMarginEnd
        )
        borderTop = DividerDrawable.from(
            attr, widthDefault,
            R.styleable.WidgetLayout_borderTop,
            R.styleable.WidgetLayout_borderTopWidth,
            R.styleable.WidgetLayout_borderTopMargin,
            R.styleable.WidgetLayout_borderTopMarginStart,
            R.styleable.WidgetLayout_borderTopMarginEnd
        )
        borderRight = DividerDrawable.from(
            attr, widthDefault,
            R.styleable.WidgetLayout_borderRight,
            R.styleable.WidgetLayout_borderRightWidth,
            R.styleable.WidgetLayout_borderRightMargin,
            R.styleable.WidgetLayout_borderRightMarginStart,
            R.styleable.WidgetLayout_borderRightMarginEnd
        )
        borderBottom = DividerDrawable.from(
            attr, widthDefault,
            R.styleable.WidgetLayout_borderBottom,
            R.styleable.WidgetLayout_borderBottomWidth,
            R.styleable.WidgetLayout_borderBottomMargin,
            R.styleable.WidgetLayout_borderBottomMarginStart,
            R.styleable.WidgetLayout_borderBottomMarginEnd
        )
        dividerHorizontal = DividerDrawable.from(
            attr, widthDefault,
            R.styleable.WidgetLayout_dividerHorizontalDrawable,
            R.styleable.WidgetLayout_dividerWidthHorizontal,
            R.styleable.WidgetLayout_dividerPaddingHorizontal,
            R.styleable.WidgetLayout_dividerPaddingHorizontalStart,
            R.styleable.WidgetLayout_dividerPaddingHorizontalEnd
        )
        dividerVertical = DividerDrawable.from(
            attr, widthDefault,
            R.styleable.WidgetLayout_dividerVerticalDrawable,
            R.styleable.WidgetLayout_dividerWidthVertical,
            R.styleable.WidgetLayout_dividerPaddingVertical,
            R.styleable.WidgetLayout_dividerPaddingVerticalStart,
            R.styleable.WidgetLayout_dividerPaddingVerticalEnd
        )
        if (attr != null) {
            val itemMargin = attr.getDimensionPixelSize(R.styleable.WidgetLayout_itemMargin, 0)
            val margin = attr.getDimensionPixelSize(R.styleable.WidgetLayout_contentMargin, 0)
            val marginH = attr.getDimensionPixelSize(R.styleable.WidgetLayout_contentMarginHorizontal, margin)
            val marginV = attr.getDimensionPixelSize(R.styleable.WidgetLayout_contentMarginVertical, margin)
            val hasItemMargin = attr.hasValue(R.styleable.WidgetLayout_itemMargin)
            val hasMargin = attr.hasValue(R.styleable.WidgetLayout_contentMargin)
            val hasMarginH = hasMargin || attr.hasValue(R.styleable.WidgetLayout_contentMarginHorizontal)
            val hasMarginV = hasMargin || attr.hasValue(R.styleable.WidgetLayout_contentMarginVertical)
            contentMargin.left = attr.getDimensionPixelSize(R.styleable.WidgetLayout_contentMarginLeft, if (hasMarginH) marginH else contentMargin.left)
            contentMargin.top = attr.getDimensionPixelSize(R.styleable.WidgetLayout_contentMarginTop, if (hasMarginV) marginV else contentMargin.top)
            contentMargin.right = attr.getDimensionPixelSize(R.styleable.WidgetLayout_contentMarginRight, if (hasMarginH) marginH else contentMargin.right)
            contentMargin.bottom = attr.getDimensionPixelSize(R.styleable.WidgetLayout_contentMarginBottom, if (hasMarginV) marginV else contentMargin.bottom)
            itemMarginHorizontal = attr.getDimensionPixelSize(R.styleable.WidgetLayout_itemMarginHorizontal, if (hasItemMargin) itemMargin else itemMarginHorizontal)
            itemMarginVertical = attr.getDimensionPixelSize(R.styleable.WidgetLayout_itemMarginVertical, if (hasItemMargin) itemMargin else itemMarginVertical)
        }
    }

    fun drawBorder(canvas: Canvas, viewWidth: Int, viewHeight: Int, offsetX: Int, offsetY: Int) {
        if (borderLeft.isValidated) {
            borderLeft.draw(canvas, offsetY, viewHeight + offsetY, offsetX + borderLeft.dividerWidth / 2f, false)
        }
        if (borderRight.isValidated) {
            borderRight.draw(canvas, offsetY, viewHeight + offsetY, offsetX + viewWidth - borderRight.dividerWidth / 2f, false)
        }
        if (borderTop.isValidated) {
            borderTop.draw(canvas, offsetX, offsetX + viewWidth, offsetY + borderTop.dividerWidth / 2f, true)
        }
        if (borderBottom.isValidated) {
            borderBottom.draw(canvas, offsetX, offsetX + viewWidth, offsetY + viewHeight - borderBottom.dividerWidth / 2f, true)
        }
    }

    fun drawDivider(canvas: Canvas, from: Int, to: Int, start: Int, horizontal: Boolean) {
        val drawer = if (horizontal) dividerHorizontal else dividerVertical
        drawer.draw(canvas, from, to, start - drawer.dividerWidth / 2f, horizontal)
    }

    fun setContentMarginHorizontal(contentMarginHorizontal: Int) {
        contentMargin.left = contentMarginHorizontal
        contentMargin.right = contentMarginHorizontal
    }

    fun setContentMarginVertical(contentMarginVertical: Int) {
        contentMargin.top = contentMarginVertical
        contentMargin.bottom = contentMarginVertical
    }

    fun setContentMargin(left: Int, top: Int, right: Int, bottom: Int) {
        contentMargin.set(left, top, right, bottom)
    }

    fun setContentMarginLeft(left: Int) {
        contentMargin.left = left
    }

    fun setContentMarginTop(top: Int) {
        contentMargin.top = top
    }

    fun setContentMarginRight(right: Int) {
        contentMargin.right = right
    }

    fun setContentMarginBottom(bottom: Int) {
        contentMargin.bottom = bottom
    }

    fun setItemMarginBetween(itemMarginBetween: Int) {
        itemMarginHorizontal = itemMarginBetween
        itemMarginVertical = itemMarginBetween
    }

    fun applyContentMargin(outRect: Rect) {
        outRect.left += contentMargin.left
        outRect.top += contentMargin.top
        outRect.right += contentMargin.right
        outRect.bottom += contentMargin.bottom
    }

    companion object {
        fun from(attr: TypedArray?, dividerWidthDefault: Int): BorderDivider {
            return BorderDivider(attr, dividerWidthDefault)
        }
    }
}