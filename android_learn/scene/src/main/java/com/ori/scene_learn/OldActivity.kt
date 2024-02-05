package com.ori.scene_learn

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.scene.NavigationSceneUtility
import com.bytedance.scene.SceneDelegate

/**
 * Created by huangguocheng on 2024/2/4
 * @author huangguocheng@bytedance.com
 */
class OldActivity : AppCompatActivity() {

    private lateinit var mDelegate: SceneDelegate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = (window.decorView.systemUiVisibility
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }

        mDelegate = NavigationSceneUtility.setupWithActivity(this, savedInstanceState, TargetScene::class.java, false)
    }

    override fun onBackPressed() {
        if (!mDelegate.onBackPressed()) {
            super.onBackPressed()
        }
    }
}