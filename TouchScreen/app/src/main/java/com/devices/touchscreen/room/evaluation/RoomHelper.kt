package com.devices.touchscreen.room.evaluation

import androidx.room.Room
import com.devices.touchscreen.base.MyApplication
import com.devices.touchscreen.bean.EvaluateDetailBean

/**
 * 作者：create by comersss on 2020/9/10 11:55
 * 邮箱：904359289@qq.com
 *
 */
object RoomHelper {

    private val appDatabase by lazy {
        Room.databaseBuilder(MyApplication.instance, AppDatabase::class.java, "database_evaluatedetail").build()
    }

    private val readHistoryDao by lazy { appDatabase.readHistoryDao() }

    fun queryAllReadHistory() = readHistoryDao.queryAll()

    fun addReadHistory(article: EvaluateDetailBean) {
        readHistoryDao.queryArticle(article.AuthCode)?.let {
            readHistoryDao.deleteArticle(it)
        }
        readHistoryDao.insert(article)
    }

    fun deleteReadHistory(article: EvaluateDetailBean) = readHistoryDao.deleteArticle(article)
}