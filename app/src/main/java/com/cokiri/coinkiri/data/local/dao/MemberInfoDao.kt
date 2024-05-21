package com.cokiri.coinkiri.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity

@Dao
interface MemberInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemberInfo(memberInfo: MemberInfoEntity)

    @Update
    suspend fun updateMemberInfo(memberInfo: MemberInfoEntity)

    @Query("SELECT * FROM member_info WHERE id = :id LIMIT 1")
    suspend fun getMemberInfoById(id: String): MemberInfoEntity?

    @Query("DELETE FROM member_info")
    suspend fun deleteAll()

}