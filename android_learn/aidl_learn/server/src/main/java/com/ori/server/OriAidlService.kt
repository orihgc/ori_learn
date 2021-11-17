package com.ori.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ori.Server.IOriAidl

class OriAidlService : Service() {

    val persons = mutableListOf<Person>()
    private val iBinder = object : IOriAidl.Stub() {
        override fun addPerson(person: Person?) {
            person?.let { persons.add(it) }
        }

        override fun getPsersonList(): MutableList<Person> {
            return persons
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return iBinder
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("OriAidlService","onCreate:success")
    }
}