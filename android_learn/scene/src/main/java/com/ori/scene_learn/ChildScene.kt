package com.ori.scene_learn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bytedance.scene.Scene

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
class ChildScene : Scene() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, p2: Bundle?): ViewGroup {
        return (inflater.inflate(R.layout.child_scene, container, false) as? ViewGroup) ?: container
    }
}