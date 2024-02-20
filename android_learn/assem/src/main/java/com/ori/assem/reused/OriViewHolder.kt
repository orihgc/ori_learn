package com.ori.assem.reused

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.ext_power_list.AssemReusedVHContainer
import com.bytedance.ext_power_list.IAssemReusedContainer
import com.bytedance.tiktok.proxy.IVMProxyHolder
import com.bytedance.tiktok.proxy.ReusedItem
import com.bytedance.tiktok.proxy.list.AssemReusedVHProxyer

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class OriViewHolder(
    view: View,
    proxyer: AssemReusedVHProxyer<IVMProxyHolder<*, *>, ReusedItem>,
    lifecycleOwner: LifecycleOwner
) : IAssemReusedContainer<OriViewHolder, OriItem> by AssemReusedVHContainer(lifecycleOwner, proxyer, OriRootAssem(), view),
    RecyclerView.ViewHolder(view) {

    init {
        onCreateItemView(view, this)
    }
}