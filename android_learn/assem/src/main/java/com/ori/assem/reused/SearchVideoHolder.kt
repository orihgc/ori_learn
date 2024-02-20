package com.ori.assem.reused

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.bytedance.ext_power_list.AssemReusedVHContainer
import com.bytedance.ext_power_list.IAssemReusedContainer
import com.bytedance.tiktok.proxy.IVMProxyHolder
import com.bytedance.tiktok.proxy.ReusedItem
import com.bytedance.tiktok.proxy.list.AssemReusedVHProxyer

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class SearchVideoHolder(
    view: View,
    proxyer: AssemReusedVHProxyer<IVMProxyHolder<*, *>, ReusedItem>,
    lifecycle: LifecycleOwner
) : IAssemReusedContainer<SearchVideoHolder, SearchVideoItem> by AssemReusedVHContainer(
    lifecycle,
    proxyer,
    SearchRootAssem(), // the assem of this ViewHolder
    view
) {
    init {
        onCreateItemView(view, this)
    }
}