package com.devices.touchscreen.ui

import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseVmActivity
import com.devices.touchscreen.viewmodel.ComplaintsViewModel

class ComplaintsActivity : BaseVmActivity<ComplaintsViewModel>(R.layout.activity_complaints) {
    override fun viewModelClass() = ComplaintsViewModel::class.java
}