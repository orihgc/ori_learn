package com.ori.ioc_learn

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

@SuppressLint("NonConstantResourceId")
@InjectLayout(value = R.layout.activity_main)
class MainActivity : AppCompatActivity() {

    @InjectView(value = R.id.btn_p)
    var btn: Button? = null

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InjectManager.inject(this)
    }

    @OnClick(value = R.id.btn_p)
    fun clickBtnP() {
        Toast.makeText(this, "inject view", Toast.LENGTH_SHORT).show()
    }
}