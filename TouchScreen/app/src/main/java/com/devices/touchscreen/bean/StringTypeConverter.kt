package com.devices.touchscreen.bean

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringTypeConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String) = Gson().fromJson<HashMap<String, Int>>(data, object : TypeToken<HashMap<String, Int>>() {}.type)

    @TypeConverter
    fun someObjectListToString(someObjects: HashMap<String?, Int?>?) = Gson().toJson(someObjects)
}