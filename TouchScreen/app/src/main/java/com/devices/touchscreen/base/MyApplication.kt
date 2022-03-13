package com.devices.touchscreen.base

import android.app.Application
import com.blankj.utilcode.util.ProcessUtils
import com.devices.touchscreen.common.ActivityHelper

class MyApplication : Application() {
    companion object {
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (ProcessUtils.isMainProcess()) {
            ActivityHelper.init(this)
        }
    }


}