package com.cokiri.coinkiri.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cokiri.coinkiri.data.local.dao.MemberInfoDao
import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity

@Database(entities = [MemberInfoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberInfoDao(): MemberInfoDao
}