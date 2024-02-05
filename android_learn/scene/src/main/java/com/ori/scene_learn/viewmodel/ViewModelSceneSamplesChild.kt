package com.ori.scene_learn.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.bytedance.scene.Scene
import com.bytedance.scene.ktx.activityViewModels

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
class ViewModelSceneSamplesChild : Scene() {
    private val viewModel: SampleViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return Button(requireSceneContext()).apply {
            text = "Click to +1"
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireView().setOnClickListener {
            val countValue = viewModel.counter.value ?: 0
            viewModel.counter.value = countValue + 1
        }
    }
}