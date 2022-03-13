package com.devices.touchscreen.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus


object BusHelper {

    inline fun <reified T> observe(channel: String, owner: LifecycleOwner, observer: Observer<T>) {
        LiveEventBus.get(channel, T::class.java).observe(owner, observer)
    }

    inline fun <reified T> post(channel: String, value: T) {
        LiveEventBus.get(channel, T::class.java).post(value)
    }

}