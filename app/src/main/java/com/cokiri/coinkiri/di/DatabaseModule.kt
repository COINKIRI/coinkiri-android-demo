package com.cokiri.coinkiri.di

import android.content.Context
import androidx.room.Room
import com.cokiri.coinkiri.data.local.dao.MemberInfoDao
import com.cokiri.coinkiri.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "coinkiri_database"
        ).build()
    }

    @Provides
    fun provideMemberInfoDao(appDatabase: AppDatabase): MemberInfoDao {
        return appDatabase.memberInfoDao()
    }

}