package com.devices.touchscreen.viewmodel

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.FileIOUtils
import com.devices.touchscreen.base.BaseViewModel
import com.devices.touchscreen.base.MyApplication
import com.devices.touchscreen.bean.BannerBean
import com.devices.touchscreen.bean.RestInfoBean
import com.devices.touchscreen.common.RestDirection
import com.devices.touchscreen.common.RestId
import com.devices.touchscreen.net.RetrofitClient
import java.io.File

class MainViewModel : BaseViewModel() {
    val publicPagePhoto = MutableLiveData<BannerBean>()
    val restInfoResult = MutableLiveData<RestInfoBean>()
    val downloadCodeResult = MutableLiveData<String>()
    val filePath: String = MyApplication.instance.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath.toString() +
            File.separator + "ysx.png"

    fun getInfo() {
        if (RestId.isEmpty()) {
            return
        }
        launch({
            RetrofitClient.apiService.downloadCode(RestId, RestDirection).byteStream().let {
                FileIOUtils.writeFileFromIS(File(filePath), it).apply {
                    if (this) {
                        downloadCodeResult.value = filePath
                    }
                }
            }
            restInfoResult.value = RetrofitClient.apiService.getRestInfo(RestId, RestDirection).apiData()
            publicPagePhoto.value = RetrofitClient.apiService.getPublicPagePhoto(RestId, RestDirection).apiData()
        })
    }
}