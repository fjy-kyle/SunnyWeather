package com.sunnyweather.android.logic.model


// Weather封装Daily和Realtime
data class Weather(val daily: DailyResponse.Daily, val realtime: RealtimeResponse.Realtime)