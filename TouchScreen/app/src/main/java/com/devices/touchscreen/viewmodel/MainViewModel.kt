package com.devices.touchscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.devices.touchscreen.base.BaseViewModel
import com.devices.touchscreen.bean.BannerBean
import com.devices.touchscreen.bean.RestInfoBean
import com.devices.touchscreen.common.RestDirection
import com.devices.touchscreen.common.RestId
import com.devices.touchscreen.net.RetrofitClient

class MainViewModel : BaseViewModel() {
    val publicPagePhoto = MutableLiveData<BannerBean>()
    val restInfoResult = MutableLiveData<RestInfoBean>()
    val downloadCodeResult = MutableLiveData<String>()
    fun getInfo() {
        if (RestId.isEmpty()) {
            return
        }
        launch({
//            downloadCodeResult.value = RetrofitClient.apiService.downloadCode(RestId, RestDirection).apiData()
            restInfoResult.value = RetrofitClient.apiService.getRestInfo(RestId, RestDirection).apiData()
            publicPagePhoto.value = RetrofitClient.apiService.getPublicPagePhoto(RestId, RestDirection).apiData()
        })
    }
}