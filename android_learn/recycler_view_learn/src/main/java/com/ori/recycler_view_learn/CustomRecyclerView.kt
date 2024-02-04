package com.ori.recycler_view_learn

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by huangguocheng on 2024/1/24
 * @author huangguocheng@bytedance.com
 */
class CustomRecyclerView(context: Context) : RecyclerView(context) {


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
    }

    override fun onViewRemoved(child: View?) {
        super.onViewRemoved(child)
    }


}