package com.ori.retrofit_learn

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

fun main() {
    val retrofit = Retrofit.Builder().baseUrl("www.baidu.com").build()
    val service = retrofit.create(ApiService::class.java)
    val call = service.fetchData()
    call.enqueue(object :Callback<Any>{
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            println()
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
        }

    })
}