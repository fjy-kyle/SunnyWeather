package com.sunnyweather.android.logic.network

import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.PlaceResponse
import okhttp3.Call
import retrofit2.http.GET
import retrofit2.http.Query

// 访问彩云天气城市搜索API的Retrofit接口
interface PlaceService {

    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    // query参数是要查询的关键字,如：“北京”
    fun searchPlaces(@Query("query") query: String): retrofit2.Call<PlaceResponse>
}