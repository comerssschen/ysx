package com.devices.touchscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.devices.touchscreen.base.BaseViewModel
import com.devices.touchscreen.bean.BannerBean
import com.devices.touchscreen.bean.DropDownBean
import com.devices.touchscreen.common.RestDirection
import com.devices.touchscreen.common.RestId
import com.devices.touchscreen.net.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ConfigViewModel : BaseViewModel() {

    val DropDownResult = MutableLiveData<ArrayList<DropDownBean>>()
    fun getDropdown() {
        launch({
            DropDownResult.value = RetrofitClient.apiService.getDropdown().apiData()
        }, isShowLoadding = false)
    }

    val DirectionDropDownResult = MutableLiveData<ArrayList<DropDownBean>>()
    fun getRestDirectionDropDown(restId: String) {
        launch({
            DirectionDropDownResult.value = RetrofitClient.apiService.getRestDirectionDropDown(restId).apiData()
        }, isShowLoadding = false)
    }


    val submitConfigResult = MutableLiveData<Boolean>()
    fun submitConfig(restId: String, restDirection: String, publicFrontPageList: Array<String>, inPage: String) {
        launch({
            RetrofitClient.apiService.submitConfig(
                mapOf(
                    "restId" to restId, "restDirection" to restDirection, "publicFrontPageList" to publicFrontPageList, "inPage" to inPage
                )
            ).apiData()
            RestId = restId
            RestDirection = restDirection
            submitConfigResult.value = true
        })
    }

    val uploadImageResult = MutableLiveData<String>()
    fun uploadImage(file: File) {
        val fileRQ = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("UploadFile", file.name, fileRQ);
        launch({
            uploadImageResult.value = RetrofitClient.apiService.uploadImage(part).apiData()?.fileUrl
        })
    }

    val publicPagePhoto = MutableLiveData<BannerBean>()
    fun getInfo(restId: String = RestId, restDirection: String = RestDirection) {
        if (restId.isNotEmpty()) {
            launch({
                publicPagePhoto.value = RetrofitClient.apiService.getPublicPagePhoto(restId, restDirection).apiData()
            }, isShowLoadding = false)
        }
    }
}