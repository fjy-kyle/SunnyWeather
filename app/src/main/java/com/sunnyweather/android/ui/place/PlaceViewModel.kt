package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Place
import com.sunnyweather.android.logic.Repository

class PlaceViewModel : ViewModel() {

    // 保存query（查询的关键字）
    private val searchLiveData = MutableLiveData<String>()

    // 缓存界面上显示的地区Place数据
    val placeList = ArrayList<Place>()

    // 用户输入关键字触发观察，发起网络请求返回与关键字相关的
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    // 将传入的搜索参数赋值给searchLiveData
    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }
}