package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException

// 仓库层的统一封装入口
object Repository {

    // 返回一个LiveData,此处返回的是LiveData<List<Place>>
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        // 根据请求返回数据给调用方
        val result = try {
            // 搜索城市query数据，将返回的数据解析进创建好的对象，这是网络真正返回的数据
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                // 此处取出返回数据中的地区Place，最终返回的是Place
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e:Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
}