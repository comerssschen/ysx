package com.devices.touchscreen.ui

import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.RegexUtils
import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseVmActivity
import com.devices.touchscreen.common.ActivityHelper
import com.devices.touchscreen.common.GlideEngine
import com.devices.touchscreen.common.showToast
import com.devices.touchscreen.common.toStr
import com.devices.touchscreen.viewmodel.ComplaintsViewModel
import kotlinx.android.synthetic.main.activity_complaints.*

class ComplaintsActivity : BaseVmActivity<ComplaintsViewModel>(R.layout.activity_complaints) {
    override fun viewModelClass() = ComplaintsViewModel::class.java

    var carType = 0
    override fun initView() {
        super.initView()
        groupComplants.isVisible = intent.getIntExtra("Type", 0) == 1
        group2.isVisible = intent.getIntExtra("Type", 0) == 2

        etOpinion.addTextChangedListener {
            tvCount.text = "${it.toString().length}/50"
        }
        intent.getStringExtra("BannerUrl")?.let { GlideEngine.createGlideEngine().loadImage(this, it, ivBanner) }
        ivHome.setOnClickListener { ActivityHelper.finish(ComplaintsActivity::class.java) }
        btSubmit.setOnClickListener {
            when {
                etName.toStr().isEmpty() -> showToast("请输入姓名")
                etPhone.toStr().isEmpty() -> showToast("请输入手机号")
                !RegexUtils.isMobileSimple(etPhone.toStr()) -> showToast("手机号格式不正确")
                etOpinion.toStr().isEmpty() -> showToast("请输入您的宝贵意见")
                else -> mViewModel.addPublicComplain(etName.toStr(), etPhone.toStr(), etOpinion.toStr())
            }

        }

        tvBigCarDriver.setOnClickListener {
            carType = 1
            notifyBg()
        }
        tvLittlecarDriver.setOnClickListener {
            carType = 2
            notifyBg()
        }
        tvBigCar.setOnClickListener {
            carType = 3
            notifyBg()
        }
        tvCarDriver.setOnClickListener {
            carType = 4
            notifyBg()
        }
        btNext.setOnClickListener {
            if (carType == 0) {
                showToast("请选择类型")
            } else {
            }
        }
    }

    private fun notifyBg() {
        tvBigCarDriver.setBackgroundResource(R.drawable.normal_bg)
        tvLittlecarDriver.setBackgroundResource(R.drawable.normal_bg)
        tvBigCar.setBackgroundResource(R.drawable.normal_bg)
        tvCarDriver.setBackgroundResource(R.drawable.normal_bg)
        when (carType) {
            1 -> tvBigCarDriver.setBackgroundResource(R.drawable.select_bg)
            2 -> tvLittlecarDriver.setBackgroundResource(R.drawable.select_bg)
            3 -> tvBigCar.setBackgroundResource(R.drawable.select_bg)
            4 -> tvCarDriver.setBackgroundResource(R.drawable.select_bg)
        }

    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            publicComplainResult.observe(this@ComplaintsActivity) {
                ActivityHelper.startActivity(ComplaintsActivity::class.java)
                ActivityHelper.finish(ComplaintsSucessActivity::class.java)
            }
        }
    }
}