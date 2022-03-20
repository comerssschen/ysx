package com.devices.touchscreen.room.complaints

import androidx.room.*
import com.devices.touchscreen.bean.AddPublicComplainBean

/**
 * 作者：create by comersss on 2020/9/10 11:56
 * 邮箱：904359289@qq.com
 *
 */
@Dao
interface ComplaintsHistoryDao {
    @Transaction
    @Insert(entity = AddPublicComplainBean::class)
    fun insert(AddPublicComplainBean: AddPublicComplainBean): Long

    @Transaction
    @Query("SELECT * FROM AddPublicComplainBean")
    fun queryAll(): List<AddPublicComplainBean>

    @Transaction
    @Query("SELECT * FROM AddPublicComplainBean WHERE AuthCode = (:id)")
    fun queryArticle(id: Long): AddPublicComplainBean?

    @Transaction
    @Delete(entity = AddPublicComplainBean::class)
    fun deleteArticle(AddPublicComplainBean: AddPublicComplainBean)

    @Transaction
    @Update(entity = AddPublicComplainBean::class)
    fun updateArticle(AddPublicComplainBean: AddPublicComplainBean)
}