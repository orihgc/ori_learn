package com.ori.scene_learn.custom.scene

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ori.scene_learn.R

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
object WidgetSceneUtility {

    private const val LIFE_CYCLE_FRAGMENT_TAG = "LifeCycleFragment"

    fun setupWithActivity(activity: FragmentActivity) {
        val supportFragmentManager = activity.supportFragmentManager
        var lifecycleFragment = supportFragmentManager.findFragmentByTag(LIFE_CYCLE_FRAGMENT_TAG) as? LifecycleFragment
        if (lifecycleFragment != null) {
            lifecycleFragment.setContainerLifecycleCallback(HostLifecycleDispatcher)
        } else {
            lifecycleFragment = LifecycleFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.content, lifecycleFragment, LIFE_CYCLE_FRAGMENT_TAG)
            transaction.commit()
            lifecycleFragment.setContainerLifecycleCallback(HostLifecycleDispatcher)
        }
    }

    fun setupWitchRecycleView(recyclerView: RecyclerView) {
        recyclerView.setRecyclerListener {
        }
    }
}