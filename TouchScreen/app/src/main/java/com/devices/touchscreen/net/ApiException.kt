package com.devices.touchscreen.net

class ApiException(var code: Int, override var message: String) : RuntimeException()