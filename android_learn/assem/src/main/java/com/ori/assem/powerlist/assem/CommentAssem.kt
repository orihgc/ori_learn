package com.ori.assem.powerlist.assem

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.bytedance.assem.arch.extensions.assemViewModel
import com.bytedance.assem.arch.reused.IListItem
import com.bytedance.assem.arch.reused.ReusedUISlotAssem
import com.ori.assem.R
import com.ori.assem.powerlist.CommentItem

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class CommentAssem : ReusedUISlotAssem<CommentAssem>(), IListItem<CommentItem> {

    private val vm: CommentVM by assemViewModel()

    override fun contentLayoutId(): Int {
        return R.layout.cell_comment_item
    }

    override fun onViewCreated(view: View) {
        val textView = view.findViewById<TextView>(R.id.tv_item)
        vm.selectSubscribe(CommentState::enable) {
            if (it){
                textView.setBackgroundColor(Color.RED)
            }else{
                textView.setBackgroundColor(Color.WHITE)
            }
        }
        textView.setOnClickListener {
            vm.click()
        }
    }

    override fun onBind(item: CommentItem) {
        val textView = contentView.findViewById<TextView>(R.id.tv_item)
        textView.text = item.content
    }
}