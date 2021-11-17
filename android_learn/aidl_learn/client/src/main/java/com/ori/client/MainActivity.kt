package com.ori.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.client.client.R
import com.ori.Server.IOriAidl
import com.ori.client.viewmodel.ClientViewModel
import com.ori.server.Person
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var iOriAidl: IOriAidl? = null

    private val clientViewModel: ClientViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        bindService()
        initListener()
    }

    private fun initViews() {
        btn.setOnClickListener {
            Log.i("", "")
            iOriAidl?.addPerson(Person().apply {
                name = "ori"
                age = 24
            })
            clientViewModel.persons.value = iOriAidl?.psersonList
        }
    }

    private fun initListener() {
        clientViewModel.persons.observe(this, Observer {
            Toast.makeText(this, "${iOriAidl?.psersonList?.size}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun bindService() {
        val intent = Intent()
        intent.component = ComponentName("com.ori.server", "com.ori.server.OriAidlService")
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iOriAidl = IOriAidl.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }
}