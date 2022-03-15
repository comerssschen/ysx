package com.devices.touchscreen.ui

import android.os.CountDownTimer
import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseActivity
import com.devices.touchscreen.common.ActivityHelper
import kotlinx.android.synthetic.main.activity_sucess.*

class ComplaintsSucessActivity : BaseActivity(R.layout.activity_sucess) {
    override fun initView() {
        super.initView()
        btFinish.setOnClickListener { ActivityHelper.finish(ComplaintsSucessActivity::class.java) }
        object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                btFinish.text = "返回首页(${millisUntilFinished / 1000}s)"
            }

            override fun onFinish() {
                ActivityHelper.finish(ComplaintsSucessActivity::class.java)
            }

        }.start()
    }
}