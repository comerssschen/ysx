package com.devices.touchscreen.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.devices.touchscreen.common.RestDirection
import com.devices.touchscreen.common.RestId

@Entity
@TypeConverters(StringTypeConverter::class)
class EvaluateDetailBean {
    var comprehensiveEvaluate: Int = 0
    var direction = RestDirection
    var evaluateDescribe: String? = null
    var evaluateDetail: HashMap<String, Int>? = null
    var evaluator: Int = 0
    var restId = RestId

    @PrimaryKey
    var AuthCode: Long = System.currentTimeMillis()
}