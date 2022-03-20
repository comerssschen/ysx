package com.devices.touchscreen.room.complaints

import androidx.room.Room
import com.devices.touchscreen.base.MyApplication
import com.devices.touchscreen.bean.AddPublicComplainBean

/**
 * 作者：create by comersss on 2020/9/10 11:55
 * 邮箱：904359289@qq.com
 *
 */
object RoomComplaintsHelper {

    private val appDatabase by lazy {
        Room.databaseBuilder(MyApplication.instance, ComplaintsDatabase::class.java, "database_complaints").build()
    }

    private val readHistoryDao by lazy { appDatabase.readHistoryDao() }

    fun queryAllReadHistory() = readHistoryDao.queryAll()

    fun addReadHistory(article: AddPublicComplainBean) {
        readHistoryDao.queryArticle(article.AuthCode)?.let {
            readHistoryDao.deleteArticle(it)
        }
        readHistoryDao.insert(article)
    }

    fun deleteReadHistory(article: AddPublicComplainBean) = readHistoryDao.deleteArticle(article)
}