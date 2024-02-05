package com.ori.scene_learn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ori.scene_learn.databinding.MainActivityBinding

/**
 * Created by huangguocheng on 2024/2/4
 * @author huangguocheng@bytedance.com
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.btnSingleActivity.setOnClickListener {
            startActivity(Intent(this, SceneSingleActivity::class.java))
        }
        binding.btnOldActivity.setOnClickListener {
            startActivity(Intent(this, OldActivity::class.java))
        }
    }

}