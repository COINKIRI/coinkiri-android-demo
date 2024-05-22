package com.cokiri.coinkiri.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cokiri.coinkiri.data.local.dao.MemberInfoDao
import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity

@Database(entities = [MemberInfoEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberInfoDao(): MemberInfoDao


}