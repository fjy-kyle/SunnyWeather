package com.sunnyweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {

    // 获取PlaceService接口的动态代理对象
    private val placeService = ServiceCreator.create<PlaceService>()

    // 获取WeatherService接口的动态代理对象
    private val weatherService = ServiceCreator.create<WeatherService>()

    // 传入query参数,返回装有解析数据的PlaceResponse对象
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    // 传入经纬度，返回装有解析数据的DailyWeather对象
    suspend fun getDailyWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat).await()

    // 传入经纬度，返回装有解析数据的RealtimeWeather对象
    suspend fun getRealtimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng, lat).await()

    // 发起网络请求并返回数据方法实现
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body != null) {
                        continuation.resume(body)
                    }
                    else continuation.resumeWithException(
                        RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }

    }
}