package com.devices.touchscreen.ui

import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseVmActivity
import com.devices.touchscreen.viewmodel.EvaluationViewModel

class EvaluationActivity : BaseVmActivity<EvaluationViewModel>(R.layout.activity_evaluation) {
    override fun viewModelClass() = EvaluationViewModel::class.java
}