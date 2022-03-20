package com.devices.touchscreen.ui

import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.TimeUtils
import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseActivity
import com.devices.touchscreen.common.ActivityHelper
import java.text.SimpleDateFormat

class SplashActivity : BaseActivity(R.layout.activity_splash) {
    override fun initView() {
        super.initView()
        if (TimeUtils.getTimeSpanByNow("2022-04-20", SimpleDateFormat("yyyy-MM-dd"), TimeConstants.DAY) > 0) {
            PermissionUtils.permissionGroup(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        window.decorView.postDelayed({
                            ActivityHelper.startActivity(MainActivity::class.java)
                            ActivityHelper.finish(SplashActivity::class.java)
                        }, 1000)
                    }

                    override fun onDenied() {
                        finish()
                    }
                }).request()
        }
    }
}