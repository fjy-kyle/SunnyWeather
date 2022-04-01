package com.sunnyweather.android.logic

import android.content.Context
import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.dao.PlaceDao
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

// 仓库层的统一封装入口
object Repository {

    // 传入搜索关键字query，返回一个LiveData,此处返回的是LiveData<List<Place>>
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        // 根据请求返回数据给调用方
        // 搜索城市query数据，将返回的数据解析进创建好的对象，这是网络真正返回的数据
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            // 此处取出返回数据中的地区Place，最终返回的是Place
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    // 传入经纬度，返回LiveData<Weather>
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO){
            // 开协程同时发起网络请求
            coroutineScope {
                val deferredRealtime = async {
                    SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
                }
                val deferredDaily = async {
                    SunnyWeatherNetwork.getDailyWeather(lng, lat)
                }
                // 取出网络返回的数据，只有两个网络请求都成功响应之后继续执行
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    val weather = Weather(dailyResponse.result.daily,realtimeResponse.result.realtime)
                    Result.success(weather)
                } else {
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realtimeResponse.status} " +
                                    "daily response status is ${dailyResponse.status}."
                        )
                    )
                }
            }
        }

    private fun <T> fire(context: CoroutineContext, block:suspend () -> Result<T>) =
        liveData(context) {
            val result = try {
                block()
            } catch (e:Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    fun savePlace(place: Place)= PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}