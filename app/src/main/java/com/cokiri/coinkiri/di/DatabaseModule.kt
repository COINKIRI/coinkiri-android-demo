package com.cokiri.coinkiri.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 스키마 변경에 따른 SQL 쿼리 작성
            db.execSQL("ALTER TABLE member_info ADD COLUMN statusMessage TEXT")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 스키마 변경에 따른 SQL 쿼리 작성
            db.execSQL("ALTER TABLE member_info ADD COLUMN followingCount INTEGER DEFAULT 0 NOT NULL")
            db.execSQL("ALTER TABLE member_info ADD COLUMN followerCount INTEGER DEFAULT 0 NOT NULL")
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "coinkiri_database"
        )
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
        .build()
    }

    @Provides
    fun provideMemberInfoDao(appDatabase: AppDatabase): MemberInfoDao {
        return appDatabase.memberInfoDao()
    }

}