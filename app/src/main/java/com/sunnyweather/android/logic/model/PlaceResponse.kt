package com.sunnyweather.android.logic

import com.google.gson.annotations.SerializedName


data class PlaceResponse(val status: String, val places: List<Place>)

// @SerializedName注解让json字段和kotlin字段建立映射
data class Place(val name: String, val location: Location,
            @SerializedName("formatted_address") val address: String)

data class Location(val lng: String, val lat: String)

