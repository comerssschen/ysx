package com.devices.touchscreen.ui

import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.devices.touchscreen.R
import com.devices.touchscreen.base.BaseActivity
import com.devices.touchscreen.common.ActivityHelper

class SplashActivity : BaseActivity(R.layout.activity_splash) {
    override fun initView() {
        super.initView()
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