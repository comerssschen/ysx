package com.devices.touchscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.devices.touchscreen.base.BaseViewModel
import com.devices.touchscreen.bean.AddPublicComplainBean
import com.devices.touchscreen.bean.DropDownBean
import com.devices.touchscreen.bean.EvaluateDetailBean
import com.devices.touchscreen.net.RetrofitClient
import com.devices.touchscreen.room.complaints.RoomComplaintsHelper
import com.devices.touchscreen.room.evaluation.RoomHelper
import kotlin.concurrent.thread

class ComplaintsViewModel : BaseViewModel() {

    val publicComplainResult = MutableLiveData<Boolean>()
    fun addPublicComplain(complainant: String, complainantPhoneNo: String, description: String) {
        val bean = AddPublicComplainBean()
        bean.complainant = complainant
        bean.complainantPhoneNo = complainantPhoneNo
        bean.description = description
        launch({
            RetrofitClient.apiService.addPublicComplain(bean).apiData()
            publicComplainResult.value = true
        }, error = {
            thread {
                RoomComplaintsHelper.addReadHistory(bean)
            }
            publicEvaluationResult.value = true
        }, showErrorToast = false)
    }

    val publicEvaluationResult = MutableLiveData<Boolean>()
    fun addPublicEvaluation(comprehensiveEvaluate: Int, type: ArrayList<DropDownBean>, evaluateDescribe: String, evaluator: Int) {
        val bean = EvaluateDetailBean()
        bean.comprehensiveEvaluate = comprehensiveEvaluate
        bean.evaluateDescribe = evaluateDescribe
        bean.evaluator = evaluator
        bean.comprehensiveEvaluate = comprehensiveEvaluate
        val map = hashMapOf<String, Int>()
        type.forEach { dropBean ->
            map[dropBean.value] = dropBean.count
        }
        bean.evaluateDetail = map
        launch({
            RetrofitClient.apiService.addPublicEvaluation(bean).apiData()
            publicEvaluationResult.value = true
        }, error = {
            thread {
                RoomHelper.addReadHistory(bean)
            }
            publicEvaluationResult.value = true
        }, showErrorToast = true)
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