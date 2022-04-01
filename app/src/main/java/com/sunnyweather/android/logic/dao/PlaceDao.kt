package com.sunnyweather.android.logic.dao

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.Place

object PlaceDao {

    // 保存place,(place)Json 转成 (place)String数据
    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    // 取出place ，(place)String 转成 (place)Json
    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    // 判断是否有数据已保存
    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() = SunnyWeatherApplication.context.
            getSharedPreferences("sunny_weather",0)
}