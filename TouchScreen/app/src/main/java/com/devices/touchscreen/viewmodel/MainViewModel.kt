package com.devices.touchscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.devices.touchscreen.base.BaseViewModel
import com.devices.touchscreen.bean.BannerBean
import com.devices.touchscreen.common.RestDirection
import com.devices.touchscreen.common.RestId
import com.devices.touchscreen.net.RetrofitClient

class MainViewModel : BaseViewModel() {
    val publicPagePhoto = MutableLiveData<BannerBean>()
    fun getPublicPagePhoto() {
        if (RestId.isEmpty()) {
            return
        }
        launch({
            publicPagePhoto.value = RetrofitClient.apiService.getPublicPagePhoto(RestId, RestDirection).apiData()
        })
    }
}