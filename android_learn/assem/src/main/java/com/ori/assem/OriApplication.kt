package com.ori.assem

import android.app.Application
import com.bytedance.ies.powerlist.Power

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class OriApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Power.init(this)
    }
}