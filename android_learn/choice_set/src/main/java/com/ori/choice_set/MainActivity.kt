package com.ori.choice_set

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ori.choice_set.databinding.MainActivityLayoutBinding
import com.ori.choice_set.widget.NovaCheckBox

/**
 * Created by huangguocheng on 2024/1/18
 * @author huangguocheng@bytedance.com
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        for (i in 0..9) {
            val novaCheckBox = NovaCheckBox(context = this)
            binding.wlContainer.addView(novaCheckBox)
        }
    }
}