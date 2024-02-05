package com.ori.scene_learn

import com.bytedance.scene.Scene
import com.bytedance.scene.ui.SceneActivity

/**
 * Created by huangguocheng on 2024/2/4
 * @author huangguocheng@bytedance.com
 */
class SceneSingleActivity : SceneActivity() {
    override fun getHomeSceneClass(): Class<out Scene> = TargetScene::class.java

    override fun supportRestore(): Boolean = false
}