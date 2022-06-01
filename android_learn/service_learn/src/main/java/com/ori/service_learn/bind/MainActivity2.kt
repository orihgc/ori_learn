package com.ori.service_learn.bind

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.ori.service_learn.R
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {

    private lateinit var myService: MyService
    private var mBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBinder = service as MyService.MyBinder
            myService = myBinder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }

        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
        }

        override fun onNullBinding(name: ComponentName?) {
            super.onNullBinding(name)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        btn1.setOnClickListener {
            val randomNum = myService.randomNum
            Toast.makeText(this,"$randomNum",Toast.LENGTH_SHORT).show()
        }
        btn2.setOnClickListener {
            unbindService(connection)
            mBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MyService::class.java)
        val bindService = bindService(intent, connection, Context.BIND_AUTO_CREATE)
        Log.d("1","onStart")
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

}