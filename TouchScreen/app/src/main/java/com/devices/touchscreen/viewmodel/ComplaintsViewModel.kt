package com.devices.touchscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.devices.touchscreen.base.BaseViewModel
import com.devices.touchscreen.common.RestDirection
import com.devices.touchscreen.common.RestId
import com.devices.touchscreen.net.RetrofitClient

class ComplaintsViewModel : BaseViewModel() {

    val publicComplainResult = MutableLiveData<Boolean>()
    fun addPublicComplain(complainant: String, complainantPhoneNo: String, description: String) {
        launch({
            RetrofitClient.apiService.addPublicComplain(
                mapOf(
                    "restId" to RestId,
                    "restDirection" to RestDirection,
                    "complainant" to complainant,
                    "complainantPhoneNo" to complainantPhoneNo,
                    "description" to description
                )
            ).apiData()
            publicComplainResult.value = true
        })
    }

}