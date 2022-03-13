package com.devices.touchscreen.common

import com.blankj.utilcode.util.SPUtils

fun isLogin() = token.isNotEmpty() && accountName.isNotEmpty()

fun loginOut() {
    SPUtils.getInstance().remove("token")
    SPUtils.getInstance().remove("mobile")
    SPUtils.getInstance().remove("pwd")
}

var token: String
    get() = SPUtils.getInstance().getString("token")
    set(value) = SPUtils.getInstance().put("token", value)

var accountName: String
    get() = SPUtils.getInstance().getString("accountName")
    set(value) = SPUtils.getInstance().put("accountName", value)

var accountPWD: String
    get() = SPUtils.getInstance().getString("accountPWD")
    set(value) = SPUtils.getInstance().put("accountPWD", value)

var userInfo: String
    get() = SPUtils.getInstance().getString("userInfo")
    set(value) = SPUtils.getInstance().put("userInfo", value)
