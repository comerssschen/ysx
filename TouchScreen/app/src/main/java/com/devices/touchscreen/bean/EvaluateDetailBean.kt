package com.devices.touchscreen.bean

import com.devices.touchscreen.common.RestDirection
import com.devices.touchscreen.common.RestId

class EvaluateDetailBean {
    var comprehensiveEvaluate: Int = 0
    var direction = RestDirection
    var evaluateDescribe: String? = null
    var evaluateDetail: Map<String, Any>? = null
    var evaluator: Int = 0
    var restId = RestId
}