package com.devices.touchscreen.common

import com.blankj.utilcode.util.SPUtils

fun isLogin() = RestId.isNotEmpty() && RestDirection.isNotEmpty()

fun loginOut() {
    SPUtils.getInstance().remove("restId")
    SPUtils.getInstance().remove("restDirection")
}

var RestId: String
    get() = SPUtils.getInstance().getString("restId")
    set(value) = SPUtils.getInstance().put("restId", value)

var RestDirection: String
    get() = SPUtils.getInstance().getString("restDirection")
    set(value) = SPUtils.getInstance().put("restDirection", value)