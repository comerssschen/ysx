package com.devices.touchscreen.bean

import com.contrarywind.interfaces.IPickerViewData

data class DropDownBean(
    val label: String,
    val value: String,
    var count: Int = 0,
) : IPickerViewData {
    override fun getPickerViewText(): String {
        return value
    }
}