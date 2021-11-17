package com.ori.client.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ori.server.Person

class ClientViewModel : ViewModel() {
    var persons: MutableLiveData<MutableList<Person>> = MutableLiveData(mutableListOf())
}