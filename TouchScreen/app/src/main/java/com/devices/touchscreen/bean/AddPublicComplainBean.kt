package com.devices.touchscreen.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devices.touchscreen.common.RestDirection
import com.devices.touchscreen.common.RestId

@Entity
class AddPublicComplainBean {
    var complainant: String? = null
    var complainantPhoneNo: String? = null
    var description: String? = null
    var restDirection = RestDirection
    var restId = RestId

    @PrimaryKey
    var AuthCode: Long = System.currentTimeMillis()
}