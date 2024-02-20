package com.ori.assem.powerlist.assem

import android.util.Log
import com.bytedance.assem.arch.reused.AssemPayLoads
import com.bytedance.ext_power_list.AssemViewModelWithItem
import com.bytedance.ext_power_list.IListVMAcceptor
import com.ori.assem.powerlist.CommentItem

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class CommentVM : IListVMAcceptor<CommentState, CommentItem>, AssemViewModelWithItem<CommentState, CommentItem>() {
    companion object {
        const val TAG = "CommentVM"
    }

    override fun defaultState(): CommentState {
        return CommentState()
    }

    override fun itemSync2StateAccept(state: CommentState, item: CommentItem, payloads: AssemPayLoads): CommentState {
        Log.d(TAG, "itemSync2StateAccept ${item.enable}")
        return state.copy(enable = item.enable)
    }

    override fun state2ItemAccept(state: CommentState, item: CommentItem): CommentItem {
        Log.d(TAG, "state2ItemAccept")
        return item.copy(enable = state.enable)
    }


    fun click() {
        withState { original ->
            item?.enable = !original.enable
            setState {
                copy(enable = !original.enable)
            }
        }
    }
}