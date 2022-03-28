package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

// 定义可实现全局调用的context
class SunnyWeatherApplication : Application(){
    companion object {
        const val TOKEN = "sQ7QBysApWAcoyh4"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}