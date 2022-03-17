package com.devices.touchscreen.viewmodel

import android.util.Log
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

    val publicEvaluationResult = MutableLiveData<Boolean>()
    fun addPublicEvaluation(satisfaction: Int, type: ArrayList<Int>, description: String) {
        var typStr = ""
        type.forEachIndexed { index, it ->
            if (it > 0) {
                typStr += "$index,"
            }
        }
        if (typStr.endsWith(",")) {
            typStr = typStr.substring(0, typStr.length - 1)
        }
        Log.i("test", "typStr= $typStr")
        launch({
            RetrofitClient.apiService.addPublicComplain(
                mapOf(
                    "restId" to RestId,
                    "restDirection" to RestDirection,
                    "satisfaction" to satisfaction,
                    "type" to typStr,
                    "otherTypeName" to "otherTypeName",
                    "description" to description,
                )
            ).apiData()
            publicEvaluationResult.value = true
        })
    }

}