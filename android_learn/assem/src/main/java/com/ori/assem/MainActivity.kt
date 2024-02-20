package com.ori.assem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ori.assem.databinding.MainActivityBinding

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
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fg_container, MainFragment())
        transaction.commit()
    }
}