package com.devices.touchscreen.room.evaluation

import androidx.room.*
import com.devices.touchscreen.bean.EvaluateDetailBean

/**
 * 作者：create by comersss on 2020/9/10 11:56
 * 邮箱：904359289@qq.com
 *
 */
@Dao
interface ReadHistoryDao {
    @Transaction
    @Insert(entity = EvaluateDetailBean::class)
    fun insert(EvaluateDetailBean: EvaluateDetailBean): Long

    @Transaction
    @Query("SELECT * FROM EvaluateDetailBean")
    fun queryAll(): List<EvaluateDetailBean>

    @Transaction
    @Query("SELECT * FROM EvaluateDetailBean WHERE AuthCode = (:id)")
    fun queryArticle(id: Long): EvaluateDetailBean?

    @Transaction
    @Delete(entity = EvaluateDetailBean::class)
    fun deleteArticle(EvaluateDetailBean: EvaluateDetailBean)

    @Transaction
    @Update(entity = EvaluateDetailBean::class)
    fun updateArticle(EvaluateDetailBean: EvaluateDetailBean)
}