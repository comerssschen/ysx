package com.devices.touchscreen.common

import android.content.Intent
import android.os.Parcelable
import java.io.Serializable

fun Intent.putExtras(vararg extras: Pair<String, Any>) {
    if (extras.isEmpty()) return
    extras.forEach { (key, value) ->
        when (value) {
            is Int -> putExtra(key, value)
            is Parcelable -> putExtra(key, value)
            is Serializable -> putExtra(key, value)
            is Boolean -> putExtra(key, value)
            is String -> putExtra(key, value)
        }
    }
}

