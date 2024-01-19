package com.ori.choice_set.layout

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.Gravity
import android.view.View
import com.ori.choice_set.R
import com.ori.choice_set.utils.ViewUtils


/**
 *
 *
 * <attr name="lineCenterHorizontal" format="boolean"></attr>
 *
 * <attr name="lineCenterVertical" format="boolean"></attr>
 *
 * <attr name="lineMinItemCount" format="integer"></attr>
 *
 * <attr name="lineMaxItemCount" format="integer"></attr>
 *
 * @author: rexy
 */
open class WrapLayout : WidgetLayout {

    protected var weightSum = 0
    protected var contentMaxWidthAccess = 0
    protected var weightView: SparseArray<View> = SparseArray(2)
    protected var lineHeight = SparseIntArray(2)
    protected var lineWidth = SparseIntArray(2)
    protected var lineItemCount = SparseIntArray(2)
    protected var lineEndIndex = SparseIntArray(2)

    //每行内容水平居中
    var eachLineCenterHorizontal = false
        set(value) {
            if (field != value) {
                field = value
                requestLayoutIfNeed()
            }
        }

    //每行内容垂直居中
    var eachLineCenterVertical = false
        set(value) {
            if (field != value) {
                field = value
                requestLayoutIfNeed()
            }
        }

    //每一行最少的Item 个数
    var eachLineMinItemCount = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayoutIfNeed()
            }
        }

    //每一行最多的Item 个数
    var eachLineMaxItemCount = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayoutIfNeed()
            }
        }


    //是否支持weight 属性。
    var supportWeight = false
        set(value) {
            if (field != value) {
                field = value
                if (weightSum > 0) {
                    requestLayoutIfNeed()
                }
            }
        }


    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context, attrs, defStyleAttr, defStyleRes
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val attr = if (attrs == null) null else context.obtainStyledAttributes(attrs, R.styleable.WrapLayout)
        if (attr != null) {
            eachLineMinItemCount = attr.getInt(R.styleable.WrapLayout_lineMinItemCount, eachLineMinItemCount)
            eachLineMaxItemCount = attr.getInt(R.styleable.WrapLayout_lineMaxItemCount, eachLineMaxItemCount)
            eachLineCenterHorizontal = attr.getBoolean(R.styleable.WrapLayout_lineCenterHorizontal, eachLineCenterHorizontal)
            eachLineCenterVertical = attr.getBoolean(R.styleable.WrapLayout_lineCenterVertical, eachLineCenterVertical)
            supportWeight = attr.getBoolean(R.styleable.WrapLayout_weightSupport, supportWeight)
            attr.recycle()
        }
    }

    private fun ifNeedNewLine(child: View, attemptWidth: Int, countInLine: Int, supportWeight: Boolean): Boolean {
        var needLine = false
        if (countInLine > 0) {
            if (countInLine >= eachLineMinItemCount) {
                if (eachLineMaxItemCount > 0 && countInLine >= eachLineMaxItemCount) {
                    needLine = true
                } else {
                    if (attemptWidth > contentMaxWidthAccess) {
                        needLine = !supportWeight
                    }
                }
            }
        }
        return needLine
    }

    private fun adjustMeasureWithWeight(measureSpec: Int, remain: Int, r: IntArray, vertical: Boolean) {
        val size = weightView.size()
        val borderDivider = borderDivider
        val itemMargin = if (vertical) borderDivider.itemMarginVertical else borderDivider.itemMarginHorizontal
        for (i in 0 until size) {
            val childIndex = weightView.keyAt(i)
            val child = weightView.get(childIndex)
            val params = child.layoutParams as WidgetLayout.LayoutParams
            val oldW = params.width
            val oldH = params.height
            var parentWidthSpec = measureSpec
            var parentHeightSpec = measureSpec
            if (vertical) {
                r[1] += itemMargin
                parentHeightSpec =
                    View.MeasureSpec.makeMeasureSpec((remain * params.weight / weightSum).toInt(), View.MeasureSpec.EXACTLY)
                if (oldH == 0) {
                    params.height = -1
                }
            } else {
                r[0] += itemMargin
                parentWidthSpec =
                    View.MeasureSpec.makeMeasureSpec((remain * params.weight / weightSum).toInt(), View.MeasureSpec.EXACTLY)
                if (oldW == 0) {
                    params.width = -1
                }
            }
            measure(child, params.position, parentWidthSpec, parentHeightSpec, 0, 0)
            insertMeasureInfo(params.width(child), params.height(child), childIndex, r, vertical)
            params.width = oldW
            params.height = oldH
        }
    }

    private fun insertMeasureInfo(itemWidth: Int, itemHeight: Int, childIndex: Int, r: IntArray, vertical: Boolean) {
        if (vertical) {
            r[0] = Math.max(r[0], itemWidth)
            r[1] += itemHeight
            var betterLineIndex = -1
            val lineSize = lineEndIndex.size()
            for (lineIndex in lineSize - 1 downTo 0) {
                val line = lineEndIndex.keyAt(lineIndex)
                if (childIndex > lineEndIndex.get(line)) {
                    betterLineIndex = lineIndex
                    break
                }
            }
            betterLineIndex += 1
            val goodLine = if (betterLineIndex < lineEndIndex.size()) lineEndIndex.keyAt(betterLineIndex) else betterLineIndex
            for (lineIndex in lineSize - 1 downTo betterLineIndex) {
                val line = lineEndIndex.keyAt(lineIndex)
                lineEndIndex.put(line + 1, lineEndIndex.get(line))
                lineItemCount.put(line + 1, lineItemCount.get(line))
                lineWidth.put(line + 1, lineWidth.get(line))
                lineHeight.put(line + 1, lineHeight.get(line))
            }
            lineEndIndex.put(goodLine, childIndex)
            lineItemCount.put(goodLine, 1)
            lineWidth.put(goodLine, itemWidth)
            lineHeight.put(goodLine, itemHeight)
        } else {
            r[0] += itemWidth
            r[1] = Math.max(r[1], itemHeight)
            lineEndIndex.put(0, Math.max(lineEndIndex.get(0), childIndex))
            lineItemCount.put(0, lineItemCount.get(0) + 1)
            lineHeight.put(0, Math.max(lineHeight.get(0), itemHeight))
            lineWidth.put(0, lineWidth.get(0) + itemWidth)
        }
    }

    override fun dispatchMeasure(widthExcludeUnusedSpec: Int, heightExcludeUnusedSpec: Int) {
        val ignoreBeyondWidth = true
        val childCount = childCount
        val borderDivider = borderDivider
        lineHeight.clear()
        lineEndIndex.clear()
        lineItemCount.clear()
        lineWidth.clear()
        weightView.clear()
        weightSum = 0
        contentMaxWidthAccess = View.MeasureSpec.getSize(widthExcludeUnusedSpec)
        val contentMaxHeightAccess = View.MeasureSpec.getSize(heightExcludeUnusedSpec)
        var lastMeasureIndex = 0
        var currentLineIndex = 0
        var currentLineMaxWidth = 0
        var currentLineMaxHeight = 0
        var currentLineItemCount = 0
        var contentWidth = 0
        var contentHeight = 0
        var childState = 0
        var itemPosition = 0
        val middleMarginHorizontal = borderDivider.itemMarginHorizontal
        val middleMarginVertical = borderDivider.itemMarginVertical
        val supportWeight =
            this.supportWeight && (eachLineMaxItemCount == 1 || eachLineMinItemCount <= 0 || eachLineMinItemCount >= childCount)
        var lastMeasureChild: View? = null
        for (childIndex in 0 until childCount) {
            val child = getChildAt(childIndex)
            if (skipChild(child)) continue
            val params = child.layoutParams as WidgetLayout.LayoutParams
            params.position = itemPosition++
            if (supportWeight && params.weight > 0) {
                weightSum += params.weight.toInt()
                weightView.put(childIndex, child)
                continue
            }
            lastMeasureIndex = childIndex
            measure(child, params.position, widthExcludeUnusedSpec, heightExcludeUnusedSpec, 0, contentHeight)
            var childWidthSpace = params.width(child)
            var childHeightSpace = params.height(child)
            childState = childState or child.measuredState
            lastMeasureChild = child
            if (ifNeedNewLine(
                    child, childWidthSpace + currentLineMaxWidth + middleMarginHorizontal, currentLineItemCount, supportWeight
                )
            ) {
                if (contentMaxHeightAccess < contentHeight + childHeightSpace + currentLineMaxHeight) {
                    measure(
                        child,
                        params.position,
                        widthExcludeUnusedSpec,
                        heightExcludeUnusedSpec,
                        0,
                        contentHeight + currentLineMaxHeight
                    )
                    childWidthSpace = params.width(child)
                    childHeightSpace = params.height(child)
                }
                if (currentLineMaxWidth <= contentMaxWidthAccess) {
                    contentWidth = Math.max(contentWidth, currentLineMaxWidth)
                } else {
                    contentWidth = if (ignoreBeyondWidth) contentMaxWidthAccess else currentLineMaxWidth
                }
                if (middleMarginVertical > 0) {
                    contentHeight += middleMarginVertical
                }
                contentHeight += currentLineMaxHeight
                lineWidth.put(currentLineIndex, currentLineMaxWidth)
                lineHeight.put(currentLineIndex, currentLineMaxHeight)
                lineItemCount.put(currentLineIndex, currentLineItemCount)
                lineEndIndex.put(currentLineIndex, childIndex - 1)
                currentLineIndex++
                currentLineItemCount = 1
                currentLineMaxWidth = childWidthSpace
                currentLineMaxHeight = childHeightSpace
            } else {
                if (currentLineItemCount > 0 && middleMarginHorizontal > 0) {
                    currentLineMaxWidth += middleMarginHorizontal
                }
                currentLineItemCount++
                currentLineMaxWidth += childWidthSpace
                currentLineMaxHeight = Math.max(currentLineMaxHeight, childHeightSpace)
            }
        }
        val weightListSize = if (supportWeight) weightView.size() else 0
        if (currentLineItemCount > 0) {
            if (currentLineMaxWidth <= contentMaxWidthAccess) {
                contentWidth = Math.max(contentWidth, currentLineMaxWidth)
            } else {
                contentWidth = contentMaxWidthAccess
                lastMeasureChild?.also {
                    val params = it.layoutParams as WidgetLayout.LayoutParams
                    if (this.supportWeight && 0f == params.weight && -2 == params.width) {
                        val remainWidth =
                            contentMaxWidthAccess - (currentLineMaxWidth - (it.layoutParams as LayoutParams).width(it))
                        val position = (it.layoutParams as LayoutParams).position
                        measure(
                            it,
                            position,
                            MeasureSpec.makeMeasureSpec(remainWidth, MeasureSpec.EXACTLY),
                            heightExcludeUnusedSpec,
                            0,
                            contentHeight
                        )
                    }
                }
            }
            contentHeight += currentLineMaxHeight
            lineWidth.put(currentLineIndex, currentLineMaxWidth)
            lineHeight.put(currentLineIndex, currentLineMaxHeight)
            lineItemCount.put(currentLineIndex, currentLineItemCount)
            lineEndIndex.put(currentLineIndex, lastMeasureIndex)
        }
        if (weightListSize > 0) {
            val allSupportWeight = lineItemCount.size() == 0
            val vertical = eachLineMaxItemCount == 1
            val measureSpec: Int
            val remain: Int
            val adjustMargin: Int
            if (vertical) {
                adjustMargin = (if (allSupportWeight) weightListSize - 1 else weightListSize) * middleMarginVertical
                remain = contentMaxHeightAccess - contentHeight - adjustMargin
                measureSpec = widthExcludeUnusedSpec
            } else {
                adjustMargin = (if (allSupportWeight) weightListSize - 1 else weightListSize) * middleMarginHorizontal
                remain = contentMaxWidthAccess - contentWidth - adjustMargin
                measureSpec = heightExcludeUnusedSpec
            }
            if (remain > weightListSize) {
                val r = IntArray(2)
                adjustMeasureWithWeight(measureSpec, remain, r, vertical)
                if (vertical) {
                    contentHeight += r[1] + adjustMargin
                    contentWidth = Math.max(contentWidth, r[0])
                } else {
                    contentWidth += r[0] + adjustMargin
                    contentHeight = Math.max(contentHeight, r[1])
                    lineWidth.put(0, lineWidth.get(0) + adjustMargin)
                }
            } else {
                val zeroSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY)
                for (i in 0 until weightListSize) {
                    weightView.valueAt(i).measure(zeroSpec, zeroSpec)
                }
            }
            weightView.clear()
        }
        setContentSize(contentWidth, contentHeight, childState)
    }

    override fun dispatchLayout(contentLeft: Int, contentTop: Int, contentWidth: Int, contentHeight: Int) {
        val borderDivider = borderDivider
        val lineCount = lineEndIndex.size()
        val gravity = this.gravity
        val lineVertical =
            eachLineCenterVertical || gravity and Gravity.VERTICAL_GRAVITY_MASK == Gravity.CENTER_VERTICAL && lineCount == 1
        val lineGravity = if (eachLineCenterHorizontal) Gravity.CENTER_HORIZONTAL else gravity
        val middleMarginHorizontal = borderDivider.itemMarginHorizontal
        val middleMarginVertical = borderDivider.itemMarginVertical
        var lineEndIndex: Int
        var lineMaxHeight: Int
        var childIndex = 0
        var lineTop = contentTop
        var lineBottom: Int
        var childLeft: Int
        var childTop: Int
        var childRight: Int
        var childBottom: Int
        for (lineIndex in 0 until lineCount) {
            lineEndIndex = this.lineEndIndex.get(lineIndex)
            lineMaxHeight = lineHeight.get(lineIndex)
            childLeft =
                ViewUtils.getContentStartH(contentLeft, contentLeft + contentWidth, lineWidth.get(lineIndex), 0, 0, lineGravity)
            lineBottom = lineTop + lineMaxHeight
            while (childIndex <= lineEndIndex) {
                val child = getChildAt(childIndex)
                if (skipChild(child)) {
                    childIndex++
                    continue
                }
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight
                val params = child.layoutParams as WidgetLayout.LayoutParams
                if (eachLineMaxItemCount == 1 && !eachLineCenterHorizontal) {
                    childLeft = ViewUtils.getContentStartH(
                        contentLeft, contentLeft + contentWidth, lineWidth.get(lineIndex), 0, 0, params.gravity
                    )
                }
                childLeft += params.leftMargin()
                childRight = childLeft + childWidth
                childTop = ViewUtils.getContentStartV(
                    lineTop,
                    lineBottom,
                    childHeight,
                    params.topMargin(),
                    params.bottomMargin(),
                    if (lineVertical) Gravity.CENTER_VERTICAL else params.gravity
                )
                childBottom = childTop + childHeight
                child.layout(childLeft, childTop, childRight, childBottom)
                childLeft = childRight + params.rightMargin()
                if (middleMarginHorizontal > 0) {
                    childLeft += middleMarginHorizontal
                }
                childIndex++
            }
            childIndex = lineEndIndex + 1
            lineTop = lineBottom + middleMarginVertical
        }
    }

    override fun doAfterDraw(canvas: Canvas, contentLeft: Int, contentTop: Int, contentWidth: Int, contentHeight: Int) {
        val borderDivider = borderDivider
        val dividerHorizontal = borderDivider.isVisibleDividerHorizontal
        val dividerVertical = borderDivider.isVisibleDividerVertical
        if (dividerHorizontal || dividerVertical) {
            val lineCount = lineEndIndex.size()
            val halfMiddleMarginHorizontal = borderDivider.itemMarginHorizontal / 2
            val halfMiddleMarginVertical = borderDivider.itemMarginVertical / 2
            val parentLeft = paddingLeft
            val parentRight = width - paddingRight
            val parentBottom = height - paddingBottom
            val contentBottomMargin = contentInset.bottom
            var lineIndex = 0
            var childIndex = 0
            var lineTop = contentTop
            var lineBottom: Int
            while (lineIndex < lineCount) {
                val lineEndIndex = this.lineEndIndex.get(lineIndex)
                lineBottom = lineTop + lineHeight.get(lineIndex) + halfMiddleMarginVertical
                if (dividerHorizontal && lineBottom + contentBottomMargin < parentBottom) {
                    borderDivider.drawDivider(canvas, parentLeft, parentRight, lineBottom, true)
                }
                if (dividerVertical && lineItemCount.get(lineIndex) > 1) {
                    val dividerTop = lineTop - halfMiddleMarginVertical
                    while (childIndex < lineEndIndex) {
                        val child = getChildAt(childIndex)
                        if (skipChild(child)) {
                            childIndex++
                            continue
                        }
                        val params = child.layoutParams as WidgetLayout.LayoutParams
                        borderDivider.drawDivider(
                            canvas,
                            dividerTop,
                            lineBottom,
                            child.right + params.rightMargin() + halfMiddleMarginHorizontal,
                            false
                        )
                        childIndex++
                    }
                }
                childIndex = lineEndIndex + 1
                lineTop = lineBottom + halfMiddleMarginVertical
                lineIndex++
            }
        }
    }
}
