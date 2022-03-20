package com.devices.touchscreen.room.evaluation

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devices.touchscreen.bean.EvaluateDetailBean

/**
 * 作者：create by comersss on 2020/9/10 11:55
 * 邮箱：904359289@qq.com
 *
 */
@Database(entities = [EvaluateDetailBean::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readHistoryDao(): ReadHistoryDao
}