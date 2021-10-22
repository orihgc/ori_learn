package com.kongming.android.younglab.notepad.page

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kongming.android.younglab.R
import com.kongming.android.younglab.notepad.bean.NoteItem

/**
 * @author sunxilin@bytedance.com
 * @since 2021/9/14
 */
class NoteListAdapter : BaseQuickAdapter<NoteItem, BaseViewHolder>(R.layout.note_item_view) , LoadMoreModule{

    override fun convert(holder: BaseViewHolder, item: NoteItem) {
        holder.getView<TextView>(R.id.txv_title).text = item.title
        holder.getView<TextView>(R.id.txv_desc).text = item.description
    }
}