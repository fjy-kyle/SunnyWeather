package com.sunnyweather.android.ui.place

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.Place
import com.sunnyweather.android.showToast
import kotlinx.android.synthetic.main.fragment_place.*
import java.util.*

class PlaceFragment : Fragment() {

    // 获取PlaceViewModel实例
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    private lateinit var adapter: PlaceAdapter

    // 加载fragment_place布局
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container,false)
    }
    // 发起搜索城市数据请求
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 配置recyclerView
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this,viewModel.placeList)
        recyclerView.adapter = adapter
        // 当搜索框的内容发生变化就获取搜索框上的新内容,否则隐藏RecyclerView,而只显示图片
        searchPlaceEdit.addTextChangedListener {
            val query = it.toString()
            if (query.isNotEmpty()) {
                // 调用PlaceViewModel的searchPlaces方法,将结果传回给placeLiveData
                viewModel.searchPlaces(query)
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        // 观察placeLiveData对象，当发生变化时且不为空则添加到placeList
        // 并通知PlaceAdapter刷新界面，显示出数据，否则发生了异常弹出提示
        viewModel.placeLiveData.observe(this) { result ->
            val place = result.getOrNull()
            if (place != null) {
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(place)
                adapter.notifyDataSetChanged()
            } else {
                "未能查询到任何地点".showToast()
                result.exceptionOrNull()?.printStackTrace()
            }
        }
    }
}