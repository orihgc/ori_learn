package com.kongming.android.younglab.notepad.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kongming.android.younglab.R

/**
 * @author sunxilin@bytedance.com
 * @since 2021/8/8
 */
class CustomLoadMoreView : BaseLoadMoreView() {
    override fun getLoadComplete(holder: BaseViewHolder): View {
        return holder.getView(R.id.fl_load_more_tip)
    }

    override fun getLoadEndView(holder: BaseViewHolder): View {
        return holder.getView(R.id.fl_load_more_no_more)
    }

    override fun getLoadFailView(holder: BaseViewHolder): View {
        return holder.getView(R.id.ll_load_more_retry)
    }

    override fun getLoadingView(holder: BaseViewHolder): View {
        return holder.getView(R.id.fl_load_more_loading)
    }

    override fun getRootView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.view_load_more, parent, false)
    }
}