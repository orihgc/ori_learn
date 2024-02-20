package com.ori.assem.reused

import com.bytedance.assem.arch.reused.AssemPayLoads
import com.bytedance.ext_power_list.AssemViewModelWithItem
import com.bytedance.ext_power_list.IListVMAcceptor

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class OriAssemVM : IListVMAcceptor<OriState, OriItem>, AssemViewModelWithItem<OriState, OriItem>() {
    override fun defaultState(): OriState {
        return OriState()
    }

    override fun itemSync2StateAccept(state: OriState, item: OriItem, payloads: AssemPayLoads): OriState {
        return state.copy(enable = item.enable)
    }

    override fun state2ItemAccept(state: OriState, item: OriItem): OriItem {
        return item
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