package com.example.okhttp_learn

import com.google.gson.Gson
import okhttp3.*
import java.security.MessageDigest


fun main() {
    val url = "http://wdj.mojichina.com/whapi/json/weather"
    val token = "b3b3e9742121dc89de10"
    val passsword = "mojijrtt20151228"
    val cityID = "2244"
    val timeStamp = (System.currentTimeMillis() / 1000).toString()
    val sign = md5(passsword + timeStamp + cityID)

    val okHttpClient = OkHttpClient()
    val urlBuilder = HttpUrl.parse(url)?.newBuilder()
    urlBuilder?.addQueryParameter("timestamp", timeStamp)
    urlBuilder?.addQueryParameter("key", sign)
    urlBuilder?.addQueryParameter("cityId", cityID)
    urlBuilder?.addQueryParameter("token", token)
    val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "")
    val request = Request.Builder().url(urlBuilder?.build()).post(body).build()
    val response = okHttpClient.newCall(request).execute()
    if (response.isSuccessful) {
        val message = response.body()?.string()
        val gson = Gson()
        val fromJson = gson.fromJson(message, WeatherCondition::class.java)

    }
}

data class WeatherCondition(val data: Condition)

data class Condition(
    val condition: Data
)

data class Data(
    val condition: String
)


fun md5(content: String): String {
    val hash = MessageDigest.getInstance("MD5").digest(content.toByteArray())
    val hex = StringBuilder(hash.size * 2)
    for (b in hash) {
        var str = Integer.toHexString(b.toInt())
        if (b < 0x10) {
            str = "0$str"
        }
        hex.append(str.substring(str.length - 2))
    }
    return hex.toString()
}