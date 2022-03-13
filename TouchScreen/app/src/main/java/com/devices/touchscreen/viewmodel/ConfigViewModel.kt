package com.devices.touchscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.devices.touchscreen.base.BaseViewModel
import com.devices.touchscreen.bean.DropDownBean
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
}