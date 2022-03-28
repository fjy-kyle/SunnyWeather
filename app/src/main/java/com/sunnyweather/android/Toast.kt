package com.sunnyweather.android

import android.widget.Toast

fun String.showToast(duration : Int = Toast.LENGTH_SHORT) {
    Toast.makeText(SunnyWeatherApplication.context,this,duration).show()
}

fun Int.showToast(duration : Int = Toast.LENGTH_SHORT) {
    Toast.makeText(SunnyWeatherApplication.context,this,duration).show()
}