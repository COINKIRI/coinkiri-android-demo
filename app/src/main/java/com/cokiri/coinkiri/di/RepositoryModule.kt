package com.cokiri.coinkiri.di

import com.cokiri.coinkiri.data.repository.CoinRepositoryImpl
import com.cokiri.coinkiri.data.repository.KakaoLoginRepositoryImpl
import com.cokiri.coinkiri.data.repository.UserRepositoryImpl
import com.cokiri.coinkiri.domain.repository.CoinRepository
import com.cokiri.coinkiri.domain.repository.KakaoLoginRepository
import com.cokiri.coinkiri.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideKakaoLoginRepository(kakaoLoginRepositoryImpl: KakaoLoginRepositoryImpl): KakaoLoginRepository {
        return kakaoLoginRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository{
        return userRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideCoinRepository(coinRepositoryImpl: CoinRepositoryImpl): CoinRepository {
        return coinRepositoryImpl
    }
}