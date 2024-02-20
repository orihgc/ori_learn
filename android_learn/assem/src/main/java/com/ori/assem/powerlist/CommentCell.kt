package com.ori.assem.powerlist

import android.view.View
import android.view.ViewGroup
import com.bytedance.ies.powerlist.PowerCell
import com.ori.assem.R
import kotlinx.android.synthetic.main.cell_comment_item.view.tv_item

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class CommentCell : PowerCell<CommentItem>() {

    override fun onCreateItemView(parent: ViewGroup): View {
        return inflateItemView(parent, R.layout.cell_comment_item)
    }

    override fun onBindItemView(t: CommentItem) {
        super.onBindItemView(t)
        itemView.tv_item.text = t.content
    }
}