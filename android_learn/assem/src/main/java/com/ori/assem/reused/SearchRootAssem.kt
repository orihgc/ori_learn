package com.ori.assem.reused

import android.view.View
import com.bytedance.assem.arch.extensions.assemble
import com.bytedance.assem.arch.reused.IListItem
import com.bytedance.assem.arch.reused.ReusedUIContentAssem
import com.bytedance.ies.powerlist.data.PowerItem

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */

class TestItem : PowerItem
class SearchRootAssem : ReusedUIContentAssem<SearchRootAssem>(),IListItem<SearchVideoItem> {

    override fun onInActive() {

    }

    override fun onViewCreated(view: View) {
        assemble {
            reusedUiContentAssem {

            }

            reusedUiSlotAssem {

            }
        }
    }

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)
    }

    override fun onBind(item: SearchVideoItem) {

    }

}