package com.ori.jni_learn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ori.jni_learn.databinding.MainActivityLayoutBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val jniTest = JniTest()
        binding.tvTest.text = JavaJniTest.stringFromJNI()
    }

    external fun stringFromJNI(): String


    companion object {
        init {
            System.loadLibrary("jni_learn")
        }
    }
}