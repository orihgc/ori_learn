package com.ori.retrofit_learn

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET
    fun fetchData(): Call<Any>
}