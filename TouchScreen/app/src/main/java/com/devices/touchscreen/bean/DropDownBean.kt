package com.devices.touchscreen.bean

import com.contrarywind.interfaces.IPickerViewData

data class DropDownBean(
    val label: String,
    val value: String
) : IPickerViewData {
    override fun getPickerViewText(): String {
        return value
    }
}