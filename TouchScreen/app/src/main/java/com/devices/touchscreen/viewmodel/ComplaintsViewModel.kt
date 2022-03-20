package com.devices.touchscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.devices.touchscreen.base.BaseViewModel
import com.devices.touchscreen.bean.DropDownBean
import com.devices.touchscreen.bean.EvaluateDetailBean
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
    fun addPublicEvaluation(comprehensiveEvaluate: Int, type: ArrayList<DropDownBean>, evaluateDescribe: String, evaluator: Int) {
        val bean = EvaluateDetailBean()
        bean.comprehensiveEvaluate = comprehensiveEvaluate
        bean.evaluateDescribe = evaluateDescribe
        bean.evaluator = evaluator
        bean.comprehensiveEvaluate = comprehensiveEvaluate
        val map = hashMapOf<String, Any>()
        type.forEach { dropBean ->
            map[dropBean.value] = dropBean.count
        }
        bean.evaluateDetail = map
        launch({
            RetrofitClient.apiService.addPublicEvaluation(bean).apiData()
            publicEvaluationResult.value = true
        })
    }

    val evaluationGradeResult = MutableLiveData<ArrayList<DropDownBean>>()
    fun evaluationGrade() {
        launch({
            evaluationGradeResult.value = RetrofitClient.apiService.evaluationGrade().apiData()
        }, showErrorToast = false)
    }

    val evaluatorResult = MutableLiveData<ArrayList<DropDownBean>>()
    fun evaluator() {
        launch({
            evaluatorResult.value = RetrofitClient.apiService.evaluator().apiData()
        }, showErrorToast = false, isShowLoadding = false)
    }

    val evaluateTypeResult = MutableLiveData<ArrayList<DropDownBean>>()
    fun evaluateType() {
        launch({
            evaluateTypeResult.value = RetrofitClient.apiService.evaluateType().apiData()
        }, showErrorToast = false, isShowLoadding = false)
    }
}