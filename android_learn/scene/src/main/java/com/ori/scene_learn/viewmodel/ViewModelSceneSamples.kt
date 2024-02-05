package com.ori.scene_learn.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bytedance.scene.group.GroupScene
import com.bytedance.scene.ktx.activityViewModels

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
class ViewModelSceneSamples : GroupScene() {
    private val viewModel: SampleViewModel by activityViewModels()
    private lateinit var textView: TextView
    override fun onCreateView(p0: LayoutInflater, p1: ViewGroup, p2: Bundle?): ViewGroup {
        return FrameLayout(requireSceneContext())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.counter.observe(this, Observer<Int> { t -> textView.text = "" + t })

//        add(R.id.child, ViewModelSceneSamplesChild(), "Child")
    }
}