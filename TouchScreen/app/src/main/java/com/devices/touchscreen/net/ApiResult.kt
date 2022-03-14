package com.devices.touchscreen.net

import androidx.annotation.Keep

@Keep
data class ApiResult<T>(val code: Int, val msg: String, private val data: T?) {
    fun apiData(): T? {
        if (code == 0) {
            return data
        } else {
            throw ApiException(code, msg)
        }
    }
}