package com.ori.assem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ori.assem.databinding.MainActivityBinding
import com.ori.assem.powerlist.CommentItem
import com.ori.assem.powerlist.assem.CommentCell

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class MainActivity : AppCompatActivity() {


    private lateinit var binding: MainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.powerList.registerCells(CommentCell::class.java)

        for (i in 0..100) {
            binding.powerList.state.add(CommentItem(i, false, "ori$i"))
        }
    }
}