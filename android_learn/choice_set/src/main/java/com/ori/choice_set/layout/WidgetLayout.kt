package com.ori.choice_set.layout

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.ori.choice_set.R
import com.ori.choice_set.utils.ViewUtils
import com.ori.choice_set.divider.BorderDivider
import com.ori.choice_set.drawable.AnimateOvalRoundRectDrawable
import com.ori.choice_set.drawable.OvalRoundRectDrawable


/**
 *
 *
 * 1.support gravity,maxWidth,maxHeight,widthPercent,heightPercent for itself
 * 2.all its directly child can use layout_gravity,maxWidth,maxHeight,widthPercent,heightPercent to limit its size and layout position。
 * 3.support all sides of container border and layout divider
 * 4.provide interface to resize all of its child View margin and draw any thing below or over the child
 * 5.support to take over its [.onInterceptTouchEvent] and [.onTouchEvent]
 * 6.support hover drawable animation when press just like ios
 *
 *
 *
 * subclass extends this base class  can implement [.dispatchMeasure] and [.dispatchLayout]
 * to measure and layout all its children self
 *
 *
 * most time just use this layout class and provide a LayoutManager
 *
 *
 * @author: rexy
 */
open class WidgetLayout : ViewGroup {

    /**
     * content gravity [Gravity],self maxWidth maxHeight,widthPercent,heightPercent
     */
    var gravity: Int = 0  // content gravity
        set(value) {
            if (field != value) {
                field = value
                requestLayoutIfNeed()
            }
        }

    private var maxWidth = -1 // view max width
        set(value) {
            if (field != value) {
                field = value
                requestLayoutIfNeed()
            }
        }

    var maxHeight = -1 // view max height
        set(value) {
            if (field != value) {
                field = value
                requestLayoutIfNeed()
            }
        }
    var widthPercent = 0f // width percent of its parent limit
        set(value) {
            if (field != value) {
                field = value
                requestLayoutIfNeed()
            }
        }

    var heightPercent = 0f // height percent of its parent limit
        set(value) {
            if (field != value) {
                field = value
                requestLayoutIfNeed()
            }
        }

    /**
     * set max width percent of its parent could give, this will overwrite layout_width property
     */
    var maxWidthPercent = 0f
        set(value) {
            if (field != value) {
                field = value
                requestLayoutIfNeed()
            }
        } // max width percent of its parent limit

    /**
     * set max height percent of its parent could give, this will overwrite layout_width property
     */
    var maxHeightPercent = 0f
        set(value) {
            if (field != value) {
                field = value
                requestLayoutIfNeed()
            }
        }// max height percent of its parent limit

    /**
     * equals to the xml attr contentMarginHorizontal
     */
    var contentMarginHorizontal = 0F
        set(value) {
            if (field != value) {
                field = value
                borderDivider.setContentMarginHorizontal(transferToPixels(value, TypedValue.COMPLEX_UNIT_DIP).toInt())
                requestLayoutIfNeed()
            }
        }

    private inline fun transferToPixels(data: Float, unit: Int): Float {
        return TypedValue.applyDimension(unit, data, resources.displayMetrics)
    }

    /**
     * @see .HORIZONTAL
     *
     * @see .VISIBLE
     *
     * @see .setOrientation
     * @see .onOrientationChanged
     */
    private var orientation: Int = 0

    /**
     * whether it support touch scroll action .
     */
    protected open var touchScrollEnable = false

    /**
     * EdgeEffect enable in scroll layout
     */
    protected open var edgeEffectEnable: Boolean = false

    var isCancelDrawableAnimateWhenTouchOut = true

    protected var touchSlop = 0 // touch scale slop
    /**
     * control content margin and item divider also it's margin padding
     */
    private var virtualCount = 0 // virtual child count
    private var contentLeft = 0 // content start left since the layout will be constrained by gravity
    private var contentTop = 0 // content start top since the layout will be constrained by gravity
    /**
     * get content width with inset margin
     */
    var contentWidth = 0
        private set // content width include content inset and content margin
    /**
     * get content height with inset margin
     */
    var contentHeight = 0
        private set // content height include content inset and content margin
    var measureState = 0
        private set // measure state of all children
    protected var contentInset = Rect() // content inset
    /**
     * get visible area rect exclude padding ,scrollX and scrollY are taken into account with a offset
     *
     * @see .computeVisibleBounds
     */
    var visibleContentBounds = Rect()
        protected set // visible bounds exclude padding
    /**
     * true if a layout process happened
     */
    var isAttachLayoutFinished = false
        private set // attach after layout if true
    private var itemTouchInvoked = false // flag for a processing with a series of touch event that consumed by custom handler

    //start-dev
    private var logTag: String? = null
    private var devLog = false
    //end-dev

    /**
     * provide a chance let the user to take over touch event.
     */
    private var itemTouchListener: OnItemTouchListener? = null

    /**
     * this border and divider provider which can custom and draw border and divider
     */
    lateinit var borderDivider: BorderDivider
        private set

    /**
     * a decoration interface to adjust child margin and draw some over or under the child
     */
    private var drawerDecoration: DrawerDecoration? = null

    /**
     * hove drawable that will draw over the content
     */
    /**
     * set hover drawable [AnimateOvalRoundRectDrawable]
     *
     * @param foregroundDrawable
     */
    var foregroundDrawable: AnimateOvalRoundRectDrawable? = null
        set(value) {
            if (field != value) {
                if (field != null) {
                    field!!.callback = null
                    unscheduleDrawable(field)
                }
                field = value
                if (value != null) {
                    value.callback = this
                    value.setVisible(visibility == View.VISIBLE, false)
                }
            }
        }
    private var drawableAnimateTime = 0L

    //start:log
    protected val isLogAccess: Boolean
        get() = logTag != null

    protected val isDevLogAccess: Boolean
        get() = logTag != null && devLog

    val isOrientationHorizontal: Boolean
        get() = orientation and HORIZONTAL == HORIZONTAL

    val isOrientationVertical: Boolean
        get() = orientation and VERTICAL == VERTICAL
    //end:measure&layout&draw

    //兼容BubbleLayout使用，有机会修改时直接删除
    val mItemMarginVertical: Int
        get() = borderDivider.itemMarginVertical


    // start: tool function

    /**
     * get max scroll range at direction vertical
     */
    val horizontalScrollRange: Int
        get() = Math.max(0, contentWidth - visibleContentBounds.width())

    /**
     * get max scroll range at direction vertical
     */
    val verticalScrollRange: Int
        get() = Math.max(0, contentHeight - visibleContentBounds.height())

    val itemViewCount: Int
        get() {
            var itemCount = virtualCount
            if (itemCount == 0) {
                virtualCount = getVirtualChildCount(true)
                itemCount = virtualCount
            }
            return itemCount
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

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typed1 = if (attrs == null) null else context.obtainStyledAttributes(attrs, ATTRS_PROPERTIES)
        if (typed1 != null) {
            gravity = typed1.getInteger(0, gravity)
            maxWidth = typed1.getDimensionPixelSize(1, maxWidth)
            maxHeight = typed1.getDimensionPixelSize(2, maxHeight)
            typed1.recycle()
        }
        val defaultDividerWidth = (0.5f + context.resources.displayMetrics.density * 0.4f).toInt()
        val typed2 = if (attrs == null) null else context.obtainStyledAttributes(attrs, R.styleable.WidgetLayout)
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        borderDivider = BorderDivider.from(typed2, defaultDividerWidth)
        if (typed2 != null) {
            edgeEffectEnable = typed2.getBoolean(R.styleable.WidgetLayout_edgeEffectEnable, edgeEffectEnable)
            widthPercent = typed2.getFraction(R.styleable.WidgetLayout_widthPercent, 1, 1, widthPercent)
            heightPercent = typed2.getFraction(R.styleable.WidgetLayout_heightPercent, 1, 1, heightPercent)
            orientation = typed2.getInteger(R.styleable.WidgetLayout_android_orientation, -1) + 1
            isCancelDrawableAnimateWhenTouchOut = typed2.getBoolean(R.styleable.WidgetLayout_groundCancelWhenTouchOut, isCancelDrawableAnimateWhenTouchOut)
            val stroke = typed2.getDimensionPixelSize(R.styleable.WidgetLayout_groundStroke, defaultDividerWidth)
            val radius = typed2.getDimensionPixelSize(R.styleable.WidgetLayout_groundRadius, 0)
            val radiusType = typed2.getInt(R.styleable.WidgetLayout_groundRadiusType, OvalRoundRectDrawable.RADIUS_AROUND)
            val radiusFraction = typed2.getFraction(R.styleable.WidgetLayout_groundRadiusPercentHeight, 1, 1, 0f)
            val duration = typed2.getInt(R.styleable.WidgetLayout_groundDuration, DRAWABLE_DURATION)
            val shadowColor=typed2.getColor(R.styleable.WidgetLayout_shadowColor, 0)
            val shadowWidth  = typed2.getDimensionPixelSize(R.styleable.WidgetLayout_shadowWidth, defaultDividerWidth shl 2)
            setOurForeground(typed2, stroke, radius, radiusFraction, radiusType, duration)
            setOurBackground(typed2, stroke, radius, radiusFraction, radiusType, duration,shadowColor,shadowWidth)
            typed2.recycle()
        }
        isMotionEventSplittingEnabled = false
    }

    private fun setOurForeground(typed: TypedArray, stroke: Int, radius: Int, radiusFraction: Float, radiusType: Int, duration: Int) {
        val color = typed.getColor(R.styleable.WidgetLayout_foregroundColor, 0)
        if (color != 0) {
            val fromAlpha = typed.getFraction(R.styleable.WidgetLayout_foregroundAlphaFrom, 1, 1, 0f)
            val toAlpha = typed.getFraction(R.styleable.WidgetLayout_foregroundAlphaTo, 1, 1, 0.06f)
            foregroundDrawable = AnimateOvalRoundRectDrawable(color, 0, stroke, radius, radiusFraction, radiusType).apply {
                duration(duration)
                alphaFraction(fromAlpha, toAlpha)
            }
            isClickable = fromAlpha < toAlpha && duration > 0
        }
    }

    private fun setOurBackground(typed: TypedArray, stroke: Int, radius: Int, radiusFraction: Float, radiusType: Int, duration: Int,shadowColor:Int,shadowWidth:Int) {
        val color = typed.getColor(R.styleable.WidgetLayout_backgroundColor, 0)
        val strokeColor = typed.getColor(R.styleable.WidgetLayout_backgroundStrokeColor, 0)
        if (color != 0 || (strokeColor != 0 && stroke != 0)) {
            val fromAlpha = typed.getFraction(R.styleable.WidgetLayout_backgroundAlphaFrom, 1, 1, 1f)
            val toAlpha = typed.getFraction(R.styleable.WidgetLayout_backgroundAlphaTo, 1, 1, 1f)
            val drawable = AnimateOvalRoundRectDrawable(color, strokeColor, stroke, radius, radiusFraction, radiusType, shadowColor, shadowWidth).apply {
                duration(duration)
                alphaFraction(fromAlpha, toAlpha)
                setVisible(visibility == View.VISIBLE, false)
            }
            ViewUtils.setBackground(this, drawable)
            isClickable = fromAlpha < toAlpha && duration > 0
        }
    }

    fun setLogTag(logTag: String, devMode: Boolean) {
        this.logTag = logTag
        devLog = devMode
    }

    protected fun print(category: CharSequence, msg: CharSequence) {
        print(category, msg, false)
    }

    protected fun printDev(category: CharSequence, msg: CharSequence) {
        print(category, msg, true)
    }

    private fun print(category: CharSequence?, msg: CharSequence?, dev: Boolean) {
        var msg = msg
        var tag = logTag!! + if (dev) "@" else "#"
        if (category == null || msg == null) {
            msg = category ?: msg
        } else {
            tag = tag + category
        }
        Log.d(tag, msg.toString())
    }
    //end:log

    /**
     * set layout and gesture direction
     *
     * @param orientation [.HORIZONTAL] and [.VERTICAL]
     * @see .onOrientationChanged
     */
    fun setOrientation(orientation: Int) {
        if (this.orientation != orientation) {
            val oldOrientation = this.orientation
            this.orientation = orientation
            isAttachLayoutFinished = false
            scrollTo(0, 0)
            onOrientationChanged(orientation, oldOrientation)
            requestLayout()
        }
    }

    fun getOrientation(): Int {
        return orientation
    }

    protected fun requestLayoutIfNeed() {
        if (isAttachLayoutFinished && !isLayoutRequested) {
            requestLayout()
        }
    }

    override fun removeAllViewsInLayout() {
        super.removeAllViewsInLayout()
        contentWidth = 0
        measureState = 0
        virtualCount = 0
        contentHeight = 0
        contentLeft = 0
        contentTop = 0
        isAttachLayoutFinished = false
        itemTouchInvoked = false
        contentInset.setEmpty()
        visibleContentBounds.setEmpty()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        itemTouchInvoked = false
    }

    fun setRadiusType(type: Int) {
        foregroundDrawable?.radiusType = type
        (background as? OvalRoundRectDrawable)?.radiusType = type
    }

    /**
     * set itemTouchListener , the user can handle the touch event first .
     */
    fun setOnItemTouchListener(itemTouchListener: OnItemTouchListener) {
        this.itemTouchListener = itemTouchListener
    }

    /**
     * set implement to control drawer decoration such as onDraw onDrawOver getItemOffset .
     */
    fun setDrawerDecoration(drawerDecoration: DrawerDecoration) {
        if (this.drawerDecoration !== drawerDecoration) {
            this.drawerDecoration = drawerDecoration
            requestLayoutIfNeed()
        }
    }

    /**
     * set content size after dispatch measure,so we can decide the final measure dimension
     *
     * @param contentWidth  just content width without margin and padding
     * @param contentHeight just content height without margin and padding
     * @param measureState  measure state [View.getMeasuredState]
     * @see .dispatchMeasure
     */
    protected fun setContentSize(contentWidth: Int, contentHeight: Int, measureState: Int) {
        this.contentWidth = contentWidth
        this.contentHeight = contentHeight
        this.measureState = this.measureState or measureState
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        var result = super.verifyDrawable(who)
        if (!result && this.foregroundDrawable == who) {
            result = true
        }
        return result
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): LayoutParams {
        return if (p is ViewGroup.MarginLayoutParams) {
            LayoutParams(p)
        } else LayoutParams(p)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }
    //start:measure&layout&draw

    /**
     * measure child from parent MeasureSpec
     * subclass of Layout should aways use this measure function to apply extra property such as maxWidth,maxHeight,WidgetLayout_gravity
     */
    protected fun measure(view: View, itemPosition: Int, parentWidthMeasureSpec: Int, parentHeightMeasureSpec: Int, widthUsed: Int, heightUsed: Int): LayoutParams {
        val lp = view.layoutParams as LayoutParams
        val parentWidth = View.MeasureSpec.getSize(parentWidthMeasureSpec)
        val parentHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec)
        var childWidthDimension = lp.width
        var childHeightDimension = lp.height
        if (view !is WidgetLayout) {
            if (lp.widthPercent > 0) {
                childWidthDimension = (parentWidth * lp.widthPercent).toInt()
            }
            if (lp.heightPercent > 0) {
                childHeightDimension = (parentHeight * lp.heightPercent).toInt()
            }
        }
        lp.position = itemPosition
        lp.insets.setEmpty()
        if (drawerDecoration != null) {
            drawerDecoration!!.getItemOffsets(this, view, itemPosition, lp.insets)
        }
        view.measure(
                getChildMeasureSpec(Math.max(0, parentWidth - lp.horizontalMargin() - widthUsed), View.MeasureSpec.getMode(parentWidthMeasureSpec), lp.maxWidth, childWidthDimension),
                getChildMeasureSpec(Math.max(0, parentHeight - lp.verticalMargin() - heightUsed), View.MeasureSpec.getMode(parentHeightMeasureSpec), lp.maxHeight, childHeightDimension)
        )
        return lp
    }

    private fun size(minSize: Int, maxSize: Int, contentSize: Int, unused: Int, include: Boolean): Int {
        var finalSize = contentSize + if (include) unused else 0
        finalSize = Math.max(finalSize, minSize)
        if (maxSize > 0 && maxSize < finalSize) {
            finalSize = maxSize
        }
        if (!include) {
            finalSize = Math.max(finalSize - unused, 0)
        }
        return finalSize
    }

    private fun getChildMeasureSpec(size: Int, specMode: Int, maxSize: Int, childDimension: Int): Int {
        var resultSize = size
        var resultMode = View.MeasureSpec.EXACTLY
        if (childDimension >= 0) {
            resultSize = childDimension
        } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
            resultMode = View.MeasureSpec.AT_MOST
            if (specMode == View.MeasureSpec.UNSPECIFIED && maxSize <= 0) {
                resultMode = View.MeasureSpec.UNSPECIFIED//MeasureSpec.AT_MOST ? MeasureSpec.UNSPECIFIED ?
            }
        }
        if (maxSize > 0 && resultSize > maxSize) {
            resultSize = maxSize
        }
        return View.MeasureSpec.makeMeasureSpec(resultSize, resultMode)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureState = 0
        contentHeight = measureState
        contentWidth = contentHeight
        virtualCount = contentWidth
        contentInset.setEmpty()
        if (drawerDecoration != null) {
            drawerDecoration!!.getContentOffsets(this, contentInset)
        }
        borderDivider!!.applyContentMargin(contentInset)

        val oldWidthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val oldHeightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val oldWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val oldHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        val minWidth = suggestedMinimumWidth
        val minHeight = suggestedMinimumHeight
        val maxWidth = if (this.maxWidth > 0) this.maxWidth else if (this.maxWidthPercent <= 0) -1 else (this.maxWidthPercent * oldWidth).toInt()
        val maxHeight = if (this.maxHeight > 0) this.maxHeight else if (this.maxHeightPercent <= 0) -1 else (this.maxHeightPercent * oldHeight).toInt()

        val marginH = contentInset.left + contentInset.right
        val marginV = contentInset.top + contentInset.bottom

        val paddingH = paddingLeft + paddingRight
        val paddingV = paddingTop + paddingBottom

        val width = if (widthPercent > 0) (widthPercent * oldWidth).toInt() else oldWidth
        val height = if (heightPercent > 0) (heightPercent * oldHeight).toInt() else oldHeight
        val widthMode = if (touchScrollEnable && isOrientationHorizontal) View.MeasureSpec.UNSPECIFIED else oldWidthMode
        val heightMode = if (touchScrollEnable && isOrientationVertical) View.MeasureSpec.UNSPECIFIED else oldHeightMode

        val fixedWidth = if (widthPercent > 0 || oldWidthMode == View.MeasureSpec.EXACTLY) width else 0
        val fixedHeight = if (heightPercent > 0 || oldHeightMode == View.MeasureSpec.EXACTLY) height else 0

        val adjustWidthSpec = View.MeasureSpec.makeMeasureSpec(width, oldWidthMode)
        val adjustHeightSpec = View.MeasureSpec.makeMeasureSpec(height, oldHeightMode)

        val visibleWidthSpec = View.MeasureSpec.makeMeasureSpec(size(minWidth, maxWidth, width, paddingH + marginH, false), widthMode)
        val visibleHeightSpec = View.MeasureSpec.makeMeasureSpec(size(minHeight, maxHeight, height, paddingV + marginV, false), heightMode)
        dispatchMeasure(visibleWidthSpec, visibleHeightSpec)

        val status = measureState
        val contentWidth = this.contentWidth
        val contentHeight = this.contentHeight
        setContentSize(contentWidth + marginH, contentHeight + marginV, status)

        val finalWidth = View.resolveSizeAndState(size(minWidth, maxWidth, if (fixedWidth == 0) this.contentWidth else fixedWidth - paddingH, paddingH, true), adjustWidthSpec, status)
        val finalHeight = View.resolveSizeAndState(size(minHeight, maxHeight, if (fixedHeight == 0) this.contentHeight else fixedHeight - paddingV, paddingV, true), adjustHeightSpec, status shl View.MEASURED_HEIGHT_STATE_SHIFT)

        setMeasuredDimension(finalWidth, finalHeight)

        val scrollX = scrollX
        val scrollY = scrollY
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight
        computeVisibleBounds(scrollX, scrollY, false, false)
        doAfterMeasure(measuredWidth, measuredHeight, contentWidth, contentHeight)
        if (isDevLogAccess) {
            printDev("MLD", String.format("measure: [width=%d,height=%d],[contentWidth=%d,contentHeight=%d]", measuredWidth, measuredHeight, contentWidth, contentHeight))
        }
    }

    /**
     * tips:do your measure no need to take content margin into account since we have handled.
     * after all child measure must call [.setContentSize];
     *
     * @param widthExcludeUnusedSpec  widthMeasureSpec without padding and content margin
     * @param heightExcludeUnusedSpec heightMeasureSpec without padding and content margin.
     */
    protected open fun dispatchMeasure(widthExcludeUnusedSpec: Int, heightExcludeUnusedSpec: Int) {
        val childCount = childCount
        var contentWidth = 0
        var contentHeight = 0
        var childState = 0
        var itemPosition = 0
        val itemMargin: Int
        if (isOrientationHorizontal) {
            itemMargin = borderDivider!!.itemMarginHorizontal
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (skipChild(child)) continue
                if (itemPosition != 0) contentWidth += itemMargin
                val params = measure(child, itemPosition++, widthExcludeUnusedSpec, heightExcludeUnusedSpec, 0, 0)
                contentWidth += params.width(child)
                val itemHeight = params.height(child)
                if (contentHeight < itemHeight) {
                    contentHeight = itemHeight
                }
                childState = childState or child.measuredState
            }
        } else {
            itemMargin = borderDivider!!.itemMarginVertical
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (skipChild(child)) continue
                if (itemPosition != 0) contentWidth += itemMargin
                val params = measure(child, itemPosition++, widthExcludeUnusedSpec, heightExcludeUnusedSpec, 0, 0)
                contentHeight += params.height(child)
                val itemWidth = params.width(child)
                if (contentWidth < itemWidth) {
                    contentWidth = itemWidth
                }
                childState = childState or child.measuredState
            }
        }
        setContentSize(contentWidth, contentHeight, childState)
    }

    /**
     * @param measuredWidth  self measure width
     * @param measuredHeight self measure height
     * @param contentWidth   real content width
     * @param contentHeight  real content height
     */
    protected open fun doAfterMeasure(measuredWidth: Int, measuredHeight: Int, contentWidth: Int, contentHeight: Int) {}

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val firstAttachLayout = !isAttachLayoutFinished
        isAttachLayoutFinished = true
        val inset = contentInset
        val contentWidth = this.contentWidth - (inset.left + inset.right)
        val contentHeight = this.contentHeight - (inset.top + inset.bottom)
        val unusedLeft = paddingLeft + inset.left
        val unusedRight = paddingRight + inset.right
        val unusedTop = paddingTop + inset.top
        val unusedBottom = paddingBottom + inset.bottom
        var contentLeft = ViewUtils.getContentStartH(unusedLeft, width - unusedRight, contentWidth, 0, 0, gravity)
        if (contentLeft < unusedLeft && touchScrollEnable && isOrientationHorizontal) {
            contentLeft = unusedLeft
        }
        var contentTop = ViewUtils.getContentStartV(unusedTop, height - unusedBottom, contentHeight, 0, 0, gravity)
        if (contentTop < unusedTop && touchScrollEnable && isOrientationVertical) {
            contentTop = unusedTop
        }
        this.contentLeft = contentLeft
        this.contentTop = contentTop
        dispatchLayout(contentLeft, contentTop, contentWidth, contentHeight)
        if (firstAttachLayout) {
            visibleContentBounds.offset(1, 1)
            computeVisibleBounds(scrollX, scrollY, false, true)
        }
        doAfterLayout(contentLeft, contentTop, contentWidth, contentHeight, firstAttachLayout)
        if (isDevLogAccess) {
            printDev("MLD", String.format("layout: contentLeft=%d,contentRight=%d,contentWidth=%d,contentHeight=%d, firstAttachLayout=%s", contentLeft, contentTop, contentWidth, contentHeight, firstAttachLayout))
        }
    }

    /**
     * @param contentLeft   format content's left no need to consider margin and padding of content.
     * @param contentTop    format content's top no need to consider margin and padding of content.
     * @param contentWidth  real content width exclude padding and content margin
     * @param contentHeight real content height exclude padding and content margin
     */
    protected open fun dispatchLayout(contentLeft: Int, contentTop: Int, contentWidth: Int, contentHeight: Int) {
        val count = childCount
        var childLeft = contentLeft
        var childTop = contentTop
        var childRight: Int
        var childBottom: Int
        val itemMargin: Int
        if (isOrientationHorizontal) {
            itemMargin = borderDivider!!.itemMarginHorizontal
            val baseTop = contentTop
            val baseBottom = contentTop + contentHeight
            for (i in 0 until count) {
                val child = getChildAt(i)
                if (skipChild(child)) continue
                val params = child.layoutParams as LayoutParams
                childTop = ViewUtils.getContentStartH(baseTop, baseBottom, child.measuredHeight, params.topMargin(), params.bottomMargin(), params.gravity)
                childBottom = childTop + child.measuredHeight
                childLeft += params.leftMargin()
                childRight = childLeft + child.measuredWidth
                child.layout(childLeft, childTop, childRight, childBottom)
                childLeft = childRight + params.rightMargin + itemMargin
            }
        } else {
            itemMargin = borderDivider!!.itemMarginVertical
            val baseLeft = contentLeft
            val baseRight = contentLeft + contentWidth
            for (i in 0 until count) {
                val child = getChildAt(i)
                if (skipChild(child)) continue
                val params = child.layoutParams as LayoutParams
                childTop += params.topMargin()
                childBottom = childTop + child.measuredHeight
                childLeft = ViewUtils.getContentStartH(baseLeft, baseRight, child.measuredWidth, params.leftMargin(), params.rightMargin(), params.gravity)
                childRight = childLeft + child.measuredWidth
                child.layout(childLeft, childTop, childRight, childBottom)
                childTop = childBottom + params.bottomMargin() + itemMargin
            }
        }
    }

    /**
     * @param contentLeft   format content's left no need to consider margin and padding of content.
     * @param contentTop    format content's top no need to consider margin and padding of content.
     * @param contentWidth  real content width exclude padding and content margin
     * @param contentHeight real content height exclude padding and content margin
     */
    protected open fun doAfterLayout(contentLeft: Int, contentTop: Int, contentWidth: Int, contentHeight: Int, firstAttachLayout: Boolean) {}

    public override fun dispatchDraw(canvas: Canvas) {
        if (drawerDecoration != null) {
            drawerDecoration!!.onDraw(this, canvas)
        }
        val inset = contentInset
        val width = width
        val height = height
        val contentLeft = this.contentLeft
        val contentTop = this.contentTop
        val contentWidth = this.contentWidth - (inset.left + inset.right)
        val contentHeight = this.contentHeight - (inset.top + inset.bottom)
        doBeforeDraw(canvas, contentLeft, contentTop, contentWidth, contentHeight)
        borderDivider!!.drawBorder(canvas, width, height, scrollX, scrollY)
        super.dispatchDraw(canvas)
        doAfterDraw(canvas, contentLeft, contentTop, contentWidth, contentHeight)
        doDrawOver(canvas, contentLeft, contentTop, contentWidth, contentHeight)
        if (drawerDecoration != null) {
            drawerDecoration!!.onDrawOver(this, canvas)
        }
        if (this.foregroundDrawable != null) {
            this.foregroundDrawable!!.setBounds(0, 0, width, height)
            this.foregroundDrawable!!.draw(canvas)
        }
    }

    /**
     * @param contentLeft   format content's left no need to consider margin and padding of content.
     * @param contentTop    format content's top no need to consider margin and padding of content.
     * @param contentWidth  real content width exclude padding and content margin
     * @param contentHeight real content height exclude padding and content margin
     */
    protected fun doBeforeDraw(canvas: Canvas, contentLeft: Int, contentTop: Int, contentWidth: Int, contentHeight: Int) {}

    /**
     * @param contentLeft   format content's left no need to consider margin and padding of content.
     * @param contentTop    format content's top no need to consider margin and padding of content.
     * @param contentWidth  real content width exclude padding and content margin
     * @param contentHeight real content height exclude padding and content margin
     */
    protected open fun doAfterDraw(canvas: Canvas, contentLeft: Int, contentTop: Int, contentWidth: Int, contentHeight: Int) {
        val horizontal = isOrientationHorizontal
        val count = childCount
        if (horizontal && borderDivider!!.isVisibleDividerVertical) {
            val halfMargin = borderDivider!!.itemMarginHorizontal / 2
            val start = paddingTop
            val end = height - paddingBottom
            var position: Int
            for (i in 0 until count) {
                val child = getChildAt(i)
                if (skipChild(child)) continue
                val params = child.layoutParams as LayoutParams
                position = child.right + params.rightMargin() + halfMargin
                borderDivider!!.drawDivider(canvas, start, end, position, false)
            }
        }
        if (!horizontal && borderDivider!!.isVisibleDividerHorizontal) {
            val halfMargin = borderDivider!!.itemMarginVertical / 2
            val start = paddingLeft
            val end = width - paddingRight
            var position: Int
            for (i in 0 until count) {
                val child = getChildAt(i)
                if (skipChild(child)) continue
                val params = child.layoutParams as LayoutParams
                position = child.bottom + params.bottomMargin() + halfMargin
                borderDivider!!.drawDivider(canvas, start, end, position, true)
            }
        }
    }

    protected fun doDrawOver(canvas: Canvas, contentLeft: Int, contentTop: Int, contentWidth: Int, contentHeight: Int) {}

    /**
     * compute visible bounds exclude padding and considerate scroll  x and y .
     *
     * @param scrollX       [.getScrollX]
     * @param scrollY       [.getScrollY]
     * @param scrollChanged true indicate it was called by scroll change.
     * @param apply         true to notify listener.
     */
    protected fun computeVisibleBounds(scrollX: Int, scrollY: Int, scrollChanged: Boolean, apply: Boolean) {
        val beforeHash = visibleContentBounds.hashCode()
        var width = if (apply) width else 0
        var height = if (apply) height else 0
        if (width <= 0) width = measuredWidth
        if (height <= 0) height = measuredHeight
        visibleContentBounds.left = paddingLeft + scrollX
        visibleContentBounds.top = paddingTop + scrollY
        visibleContentBounds.right = visibleContentBounds.left + width - paddingLeft - paddingRight
        visibleContentBounds.bottom = visibleContentBounds.top + height - paddingTop - paddingBottom
        if (beforeHash != visibleContentBounds.hashCode()) {
            if (!scrollChanged && !apply) {
                val adjustScrollX = Math.min(scrollX, horizontalScrollRange)
                val adjustScrollY = Math.min(scrollY, verticalScrollRange)
                if (adjustScrollX != scrollX || adjustScrollY != scrollY) {
                    scrollTo(adjustScrollX, adjustScrollY)
                }
            }
            if (apply) {
                onScrollChanged(scrollX, scrollY, visibleContentBounds, scrollChanged)
            }
            if (isDevLogAccess) {
                val sb = StringBuilder(32)
                sb.append("scrollX=").append(scrollX)
                sb.append(",scrollY=").append(scrollY).append(",visibleBounds=").append(visibleContentBounds)
                sb.append(",scrollChanged=").append(scrollChanged)
                printDev("scroll", sb)
            }
        }
    }

    private fun pointInView(localX: Float, localY: Float, slop: Float): Boolean {
        return localX >= -slop && localY >= -slop && localX < right - left + slop &&
                localY < bottom - top + slop
    }

    fun indexOfItemView(view: View?): Int {
        if (view != null) {
            var virtualIndex = 0
            val count = childCount
            for (i in 0 until count) {
                val child = getChildAt(i)
                if (skipVirtualChild(child, true)) continue
                if (view === child) {
                    return virtualIndex
                }
                virtualIndex++
            }
        }
        return -1
    }

    fun getItemView(itemIndex: Int): View? {
        var result: View? = null
        val itemCount = itemViewCount
        if (itemIndex >= 0 && itemIndex < itemCount) {
            result = getVirtualChildAt(itemIndex, true)
        }
        return result
    }

    protected fun getVirtualChildCount(withoutGone: Boolean): Int {
        var virtualCount = 0
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (skipVirtualChild(child, withoutGone)) continue
            virtualCount++
        }
        return virtualCount
    }

    fun getVirtualChildAt(virtualIndex: Int, withoutGone: Boolean): View? {
        var virtualCount = 0
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (skipVirtualChild(child, withoutGone)) continue
            if (virtualCount == virtualIndex) {
                return child
            }
            virtualCount++
        }
        return null
    }

    protected fun skipVirtualChild(child: View?, withoutGone: Boolean): Boolean {
        return child == null || withoutGone && child.visibility == View.GONE
    }

    protected fun skipChild(child: View?): Boolean {
        return child == null || child.visibility == View.GONE
    }

    //start: touch gesture

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        if (itemTouchListener != null) {
            itemTouchListener!!.onRequestDisallowInterceptTouchEvent(disallowIntercept)
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept)
    }

    /**
     * intercept touch event ,first handle by ItemTouchListener then layout manager and super will be last.
     *
     * @return true to intercept event and all event will handle by self {[.onTouchEvent]}
     */
    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        return dispatchInterceptTouchEvent(e, dispatchOnItemTouchIntercept(e)) || super.onInterceptTouchEvent(e)
    }

    protected open fun dispatchInterceptTouchEvent(e: MotionEvent, consumed: Boolean): Boolean {
        return consumed
    }

    protected fun dispatchOnItemTouchIntercept(e: MotionEvent): Boolean {
        val action = e.action
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_DOWN) {
            itemTouchInvoked = false
        }
        if (itemTouchListener != null) {
            if (itemTouchListener!!.onInterceptTouchEvent(this, e) && action != MotionEvent.ACTION_CANCEL) {
                itemTouchInvoked = true
                return true
            }
        }
        return false
    }

    /**
     * handle touch event ,first handle by ItemTouchListener then layout manager and super will be last.
     *
     * @return true to consume theme,and all event will come here again later.
     */
    override fun onTouchEvent(e: MotionEvent): Boolean {
        return dispatchTouchEvent(e, dispatchOnItemTouch(e)) || super.onTouchEvent(e)
    }

    private fun dispatchAnimateDrawable(toVisible: Boolean) {
        foregroundDrawable?.start(toVisible)
        (background as? AnimateOvalRoundRectDrawable)?.start(!toVisible)
    }

    protected open fun dispatchTouchEvent(e: MotionEvent, consumed: Boolean): Boolean {
        if (isClickable) {
            if (consumed) {
                dispatchAnimateDrawable(false)
            } else {
                val action = e.action
                if (action == MotionEvent.ACTION_DOWN) {
                    drawableAnimateTime = System.currentTimeMillis()
                    dispatchAnimateDrawable(true)
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    val durationMinRequired = DRAWABLE_DURATION
                    val durationRemain = (durationMinRequired + drawableAnimateTime - System.currentTimeMillis())
                    if (durationRemain > 0) {
                        postDelayed({
                            if (null != handler && isShown) {
                                dispatchAnimateDrawable(false)
                            }
                        }, durationRemain + 5L)
                    } else {
                        dispatchAnimateDrawable(false)
                    }
                } else if (action == MotionEvent.ACTION_MOVE) {
                    if (isCancelDrawableAnimateWhenTouchOut && !pointInView(e.x, e.y, touchSlop.toFloat())) {
                        dispatchAnimateDrawable(false)
                    }
                }
            }
        }
        return consumed
    }

    protected fun dispatchOnItemTouch(e: MotionEvent): Boolean {
        val action = e.action
        var handled = false
        if (itemTouchInvoked) {
            if (action == MotionEvent.ACTION_DOWN) {
                itemTouchInvoked = false
            } else {
                itemTouchListener!!.onTouchEvent(this, e)
                if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                    // Clean up for the next gesture.
                    itemTouchInvoked = false
                }
                handled = true
            }
        }
        if (action != MotionEvent.ACTION_DOWN && itemTouchListener != null) {
            if (itemTouchListener!!.onInterceptTouchEvent(this, e)) {
                itemTouchInvoked = true
                handled = true
            }
        }
        return handled
    }

    //end: touch gesture

    override fun onScrollChanged(l: Int, t: Int, ol: Int, ot: Int) {
        super.onScrollChanged(l, t, ol, ot)
        computeVisibleBounds(l, t, true, true)
    }

    override fun onVisibilityChanged(changedView: View, v: Int) {
        super.onVisibilityChanged(changedView, v)
        val okVisible = View.VISIBLE == visibility && View.VISIBLE == v
        foregroundDrawable?.setVisible(okVisible, false)
        (background as? AnimateOvalRoundRectDrawable)?.setVisible(okVisible, false)
    }

    protected fun onScrollChanged(scrollX: Int, scrollY: Int, visibleBounds: Rect, fromScrollChanged: Boolean) {}

    protected fun onOrientationChanged(orientation: Int, oldOrientation: Int) {}

    /**
     * custom LayoutParams which support WidgetLayout_gravity,maxWidth and maxHeight in xml attr
     * what's more,it supports inset to resize margin of child view .
     */
    open class LayoutParams : ViewGroup.MarginLayoutParams {
        var gravity = -1
        var maxWidth = -1
        var maxHeight = -1
        var weight = 0f
        var widthPercent = 0f
        var heightPercent = 0f

        val insets = Rect()
        var extras: Any? = null
        var position = -1

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {
            val a = c.obtainStyledAttributes(attrs, ATTRS_PARAMS)
            gravity = a.getInteger(0, gravity)
            maxWidth = a.getDimensionPixelSize(1, maxWidth)
            maxHeight = a.getDimensionPixelSize(2, maxHeight)
            weight = a.getFloat(3, weight)
            widthPercent = a.getFraction(5, 1, 1, widthPercent)
            heightPercent = a.getFraction(4, 1, 1, heightPercent)
            a.recycle()
        }

        constructor(width: Int, height: Int) : super(width, height) {}

        constructor(width: Int, height: Int, gravity: Int) : super(width, height) {
            this.gravity = gravity
        }

        constructor(source: ViewGroup.LayoutParams) : super(source) {}

        constructor(source: ViewGroup.MarginLayoutParams) : super(source) {
            if (source is LayoutParams) {
                gravity = source.gravity
                weight = source.weight
                maxWidth = source.maxWidth
                maxHeight = source.maxHeight
                widthPercent = source.widthPercent
                heightPercent = source.heightPercent
                extras = source.extras
                position = source.position
                insets.set(source.insets)
            } else {
                if (source is LinearLayout.LayoutParams) {
                    gravity = source.gravity
                    weight = source.weight
                }
                if (source is FrameLayout.LayoutParams) {
                    gravity = source.gravity
                }
            }
        }

        /**
         * get view width include its margin and inset width
         */
        fun width(view: View): Int {
            return view.measuredWidth + horizontalMargin()
        }

        /**
         * get view height include its margin and inset width
         */
        fun height(view: View): Int {
            return view.measuredHeight + verticalMargin()
        }

        fun leftMargin(): Int {
            return leftMargin + insets.left
        }

        fun topMargin(): Int {
            return topMargin + insets.top
        }

        fun rightMargin(): Int {
            return rightMargin + insets.right
        }

        fun bottomMargin(): Int {
            return bottomMargin + insets.bottom
        }

        fun horizontalMargin(): Int {
            return leftMargin + insets.left + rightMargin + insets.right
        }

        fun verticalMargin(): Int {
            return topMargin + insets.top + bottomMargin + insets.bottom
        }
    }

    /**
     * this interface give a chance to resize content margin and resize all its children's margin.
     * beyond that it can draw something on the canvas in this canvas coordinate
     */
    abstract class DrawerDecoration {
        open fun onDraw(parent: WidgetLayout, c: Canvas) {}

        open fun onDrawOver(parent: WidgetLayout, c: Canvas) {}

        open fun getItemOffsets(parent: WidgetLayout, child: View, itemPosition: Int, outRect: Rect) {
            outRect.set(0, 0, 0, 0)
        }

        open fun getContentOffsets(parent: WidgetLayout, outRect: Rect) {
            outRect.set(0, 0, 0, 0)
        }
    }

    /**
     * this interface provided a chance to handle touch event
     */
    interface OnItemTouchListener {
        fun onTouchEvent(parent: WidgetLayout, e: MotionEvent)

        fun onInterceptTouchEvent(parent: WidgetLayout, e: MotionEvent): Boolean

        fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean)
    }

    companion object {

        /**
         * Horizontal layout gesture and direction
         *
         * @see .setOrientation
         */
        val HORIZONTAL = 1

        /**
         * Vertical layout or gesture and direction
         *
         * @see .setOrientation
         */
        val VERTICAL = 2

        private val DRAWABLE_DURATION = 150

        private val ATTRS_PROPERTIES = intArrayOf(android.R.attr.gravity, android.R.attr.maxWidth, android.R.attr.maxHeight)

        private val ATTRS_PARAMS = intArrayOf(android.R.attr.layout_gravity, android.R.attr.maxWidth, android.R.attr.maxHeight, android.R.attr.layout_weight, R.attr.heightPercent, R.attr.widthPercent)
    }
}