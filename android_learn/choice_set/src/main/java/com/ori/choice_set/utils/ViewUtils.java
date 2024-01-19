package com.ori.choice_set.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

/**
 * @author rexy
 */
public class ViewUtils {

    public static <T extends View> T view(Activity aty, int id) {
        if (aty != null && id != View.NO_ID) {
            return (T) aty.findViewById(id);
        }
        return null;
    }

    public static <T extends View> T view(Fragment frag, int id) {
        if (frag != null && id != View.NO_ID && frag.getView() != null) {
            return (T) frag.getView().findViewById(id);
        }
        return null;
    }

    public static <T extends View> T view(View container, int id) {
        if (container != null && id != View.NO_ID) {
            return (T) container.findViewById(id);
        }
        return null;
    }

    public static <T extends View> T view(View container, Class<? extends View> cls) {
        View result = null;
        if (cls != null && cls.isAssignableFrom(container.getClass())) {
            result = container;
        } else if (container instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) container;
            int size = parent.getChildCount();
            for (int i = 0; i < size; i++) {
                if (null != (result = view(parent.getChildAt(i), cls))) {
                    break;
                }
            }
        }
        return result == null ? null : (T) result;
    }


    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    /**
     * if context is null ,a system resources will be returned not for this application.
     *
     * @param contextNullAble
     * @return wont be null
     */
    public static Resources getResource(Context contextNullAble) {
        Resources res = contextNullAble == null ? null : contextNullAble.getResources();
        if (res == null) {
            res = Resources.getSystem();
        }
        return res;
    }

    public static Drawable loadDrawable(Context context, int drawableResId) {
        Drawable drawable = null;
        if (context != null && drawableResId != 0) {
            drawable = context.getResources().getDrawable(drawableResId);
            if (drawable instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                if (bmp == null || bmp.isRecycled()) {
                    drawable = null;
                }
            }
        }
        return drawable;
    }

    public static Drawable recycleDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
            if (bmp != null && !bmp.isRecycled()) {
                bmp.recycle();
            }
            return null;
        }
        return drawable;
    }

    public static boolean isDrawableAvaiable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
            if (bmp == null || bmp.isRecycled()) {
                return false;
            }
        }
        return drawable != null;
    }

    /**
     * Runs a piece of code after the next layout run
     */
    @SuppressLint("NewApi")
    public static void doAfterLayout(final View view, final Runnable runnable) {
        OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Layout pass done, unregister for further events
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                runnable.run();
            }
        };
        view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    /**
     * @param child
     * @param maxWidth  unknown for 0.
     * @param maxHeight unknown for 0.
     * @param result    accept the width and height ,clound not be null.
     * @return int[] allow to be null.
     * @throws
     */
    public static void measureView(View child, int maxWidth, int maxHeight, int result[]) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (null == p) {
            p = new ViewGroup.LayoutParams(-2, -2);
        }
        int heightSpec;// = ViewGroup.getChildMeasureSpec(0, 0, p.height);
        int widthSpec;
        if (p.width > 0) {// exactly size
            widthSpec = MeasureSpec.makeMeasureSpec(p.width, MeasureSpec.EXACTLY);
        } else if (p.width == -2 || maxWidth <= 0) {// wrapcontent
            widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        } else if (p.width == -1) {
            widthSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
        } else {// fillparent
            widthSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST);
        }
        if (p.height > 0) {
            heightSpec = MeasureSpec.makeMeasureSpec(p.height, MeasureSpec.EXACTLY);
        } else if (p.height == -2 || maxHeight <= 0) {
            heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        } else if (p.height == -1) {
            heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
        } else {
            heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        child.measure(widthSpec, heightSpec);
        result[0] = child.getMeasuredWidth();
        result[1] = child.getMeasuredHeight();
    }

    public static void setLayerType(View v, int layerType) {
        ViewCompat.setLayerType(v, layerType, null);
    }

    @SuppressLint("NewApi")
    public static void setBackground(View v, Drawable d) {
        int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackgroundDrawable(d);
        } else {
            v.setBackground(d);
        }
    }

    /**
     * set view visible state,this will not set a same state twice or more.
     *
     * @param visible View.GONE View.VISIBLE View.INVISIBLE
     */
    public static void setVisibility(View view, int visible) {
        if (view != null && view.getVisibility() != visible) {
            view.setVisibility(visible);
        }
    }

    public static boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public static int setListViewHeightBasedOnChildren(ListView lv) {
        ListAdapter listAdapter = lv.getAdapter();
        int n = listAdapter == null ? 0 : listAdapter.getCount();
        if (n == 0) {
            return -1;
        }
        int totalHeight = 0, result[] = new int[2];
        LinearLayout ll = new LinearLayout(lv.getContext());
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View v = listAdapter.getView(i, null, lv);
            if (v instanceof RelativeLayout) {
                ll.removeAllViews();
                ll.addView(v);
                measureView(ll, 0, 0, result);
            } else {
                measureView(v, 0, 0, result);
            }
            totalHeight += result[1];
        }
        ViewGroup.LayoutParams params = lv.getLayoutParams();
        params.height = totalHeight + (lv.getDividerHeight() * (listAdapter.getCount() - 1));
        lv.setLayoutParams(params);
        return params.height;
    }

    public static Bitmap getBitmap(View v, boolean includeBar) {
        Rect r = new Rect(0, 0, 0, 0);
        if (!includeBar) {
            v.getWindowVisibleDisplayFrame(r);
        }
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight() - r.top, Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        canvas.translate(0, -r.top);
        v.draw(canvas);
        return bitmap;
    }

    public static void scrollToView(final ScrollView slv, final View dest, final int offset,
                                    int delay) {
        dest.postDelayed(new Runnable() {
            @Override
            public void run() {
                int targetScrollY = 0;
                int contentBtm = slv.getBottom();
                int targetBtm = dest.getBottom();
                targetScrollY = targetBtm - contentBtm;
                slv.smoothScrollTo(0, targetScrollY + offset);
            }
        }, Math.max(delay, 100));
    }

    public static void weightScrollChild(final ScrollView p, final float weight,
                                         final boolean adjustPadding) {
        if (p != null && p.getChildCount() > 0) {
            final Rect r = new Rect();
            p.getHitRect(r);
            if (r.isEmpty() == false) {
                r.inset((p.getPaddingLeft() + p.getPaddingRight()) / 2,
                        (p.getPaddingTop() + p.getPaddingBottom()) / 2);
                int padding = (int) ((1 - weight) * r.width()) / 2;
                if (adjustPadding) {
                    p.setPadding(padding, p.getPaddingTop(), padding, p.getPaddingBottom());
                    p.invalidate();
                } else {
                    View v = p.getChildAt(0);
                    LayoutParams lp = (LayoutParams) v.getLayoutParams();
                    lp.gravity = Gravity.CENTER_HORIZONTAL;
                    lp.rightMargin = lp.leftMargin = padding;
                    v.requestLayout();
                }
            } else {
                p.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            p.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            p.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        p.getHitRect(r);
                        int padding = (int) ((1 - weight) * r.width()) / 2;
                        if (adjustPadding) {
                            p.setPadding(padding, p.getPaddingTop(), padding, p.getPaddingBottom());
                            p.invalidate();
                        } else {
                            View v = p.getChildAt(0);
                            r.inset((p.getPaddingLeft() + p.getPaddingRight()) / 2,
                                    (p.getPaddingTop() + p.getPaddingBottom()) / 2);
                            LayoutParams lp = (LayoutParams) v.getLayoutParams();
                            lp.gravity = Gravity.CENTER_HORIZONTAL;
                            lp.rightMargin = lp.leftMargin = padding;
                            v.requestLayout();
                        }
                    }
                });
            }
        }
    }

    public static int getContentStart(int containerStart, int containerEnd, int contentWillSize, int contentGravity, boolean horizontalDirection) {
        int start = containerStart;
        if (contentGravity != -1) {
            final int mask = horizontalDirection ? Gravity.HORIZONTAL_GRAVITY_MASK : Gravity.VERTICAL_GRAVITY_MASK;
            final int maskCenter = horizontalDirection ? Gravity.CENTER_HORIZONTAL : Gravity.CENTER_VERTICAL;
            final int maskEnd = horizontalDirection ? Gravity.RIGHT : Gravity.BOTTOM;
            final int okGravity = contentGravity & mask;
            if (maskCenter == okGravity) {
                start = containerStart + (containerEnd - containerStart - contentWillSize) / 2;
            } else if (maskEnd == okGravity) {
                start = containerEnd - contentWillSize;
            }
        }
        return start;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void fixedWebViewInnerBug(View view) {
        if (view != null) {
            int sdkInt = Build.VERSION.SDK_INT;
            if (sdkInt > 10 && sdkInt < 17) {
                if (view instanceof WebView) {
                    ((WebView) view).removeJavascriptInterface("searchBoxJavaBridge_");
                } else if (view instanceof ViewGroup) {
                    ViewGroup p = (ViewGroup) view;
                    for (int i = 0; i < p.getChildCount(); i++) {
                        view = p.getChildAt(i);
                        if (view != null) {
                            //为了减少递归深度预先判断。
                            if (view instanceof WebView) {
                                fixedWebViewInnerBug(view);
                            } else if (view instanceof ViewGroup) {
                                fixedWebViewInnerBug(view);
                            }
                        }
                    }
                }
            }
        }
    }

    public static int getPointerIndex(int action) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            return (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        } else {
            return (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
        }
    }

    public static int getContentStartH(int containerLeft, int containerRight, int contentWillSize, int contentMarginLeft, int contentMarginRight, int gravity) {
        if (gravity != -1 || gravity != 0) {
            int start;
            final int mask = Gravity.HORIZONTAL_GRAVITY_MASK;
            final int maskCenter = Gravity.CENTER_HORIZONTAL;
            final int maskEnd = Gravity.RIGHT;
            final int okGravity = gravity & mask;
            if (maskCenter == okGravity) {//center
                start = (int) (containerLeft + 0.45f + (containerRight - containerLeft - (contentWillSize + contentMarginRight - contentMarginLeft)) / 2f);
            } else if (maskEnd == okGravity) {//end
                start = containerRight - contentWillSize - contentMarginRight;
            } else {//start
                start = containerLeft + contentMarginLeft;
            }
            return start;
        }
        return containerLeft + contentMarginLeft;
    }

    public static int getContentStartV(int containerTop, int containerBottom, int contentWillSize, int contentMarginTop, int contentMarginBottom, int gravity) {
        if (gravity != -1 || gravity != 0) {
            int start;
            final int mask = Gravity.VERTICAL_GRAVITY_MASK;
            final int maskCenter = Gravity.CENTER_VERTICAL;
            final int maskEnd = Gravity.BOTTOM;
            final int okGravity = gravity & mask;
            if (maskCenter == okGravity) {//center
                start = (int) (containerTop + 0.45f + (containerBottom - containerTop - (contentWillSize + contentMarginBottom - contentMarginTop)) / 2f);
            } else if (maskEnd == okGravity) {//end
                start = containerBottom - contentWillSize - contentMarginBottom;
            } else {//start
                start = containerTop + contentMarginTop;
            }
            return start;
        }
        return containerTop + contentMarginTop;
    }

    public static void addEditFilter(EditText edit, InputFilter... filters) {
        if (filters != null && filters.length > 0) {
            InputFilter[] old = edit.getFilters();
            if (old == null) {
                edit.setFilters(filters);
            } else {
                InputFilter[] merge = new InputFilter[old.length + filters.length];
                System.arraycopy(old, 0, merge, 0, old.length);
                System.arraycopy(filters, 0, merge, old.length, filters.length);
                edit.setFilters(merge);
            }
        }
    }

    public static void swap(View child1, View child2) {
        if (child1 != null && child2 != null) {
            ViewGroup parent = child1.getParent() instanceof ViewGroup ? (ViewGroup) child1.getParent() : null;
            if (parent != null && parent == child2.getParent()) {
                int index1 = parent.indexOfChild(child1);
                int index2 = parent.indexOfChild(child2);
                View minView = child1, maxView = child2;
                int minIndex = index1, maxIndex = index2;
                if (index1 > index2) {
                    minView = child2;
                    maxView = child1;
                    minIndex = index2;
                    maxIndex = index1;
                }
                parent.removeViewInLayout(minView);
                parent.removeViewInLayout(maxView);
                parent.addView(maxView, minIndex);
                parent.addView(minView, maxIndex);
            }
        }
    }

}