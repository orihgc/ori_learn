package com.ori.scene_learn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytedance.scene.group.GroupScene
import com.ori.scene_learn.databinding.TargetSceneBinding

/**
 * Created by huangguocheng on 2024/2/4
 * @author huangguocheng@bytedance.com
 */
class TargetScene : GroupScene() {

    private lateinit var binding: TargetSceneBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): ViewGroup {
        binding = TargetSceneBinding.inflate(inflater)
        return (inflater.inflate(R.layout.target_scene, container, false) as? ViewGroup) ?: container
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add(R.id.fl_container, ChildScene(), "ChildScene")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


}