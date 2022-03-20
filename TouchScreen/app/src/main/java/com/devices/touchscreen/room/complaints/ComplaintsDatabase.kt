package com.devices.touchscreen.room.complaints

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devices.touchscreen.bean.AddPublicComplainBean

/**
 * 作者：create by comersss on 2020/9/10 11:55
 * 邮箱：904359289@qq.com
 *
 */
@Database(entities = [AddPublicComplainBean::class], version = 1)
abstract class ComplaintsDatabase : RoomDatabase() {
    abstract fun readHistoryDao(): ComplaintsHistoryDao
}