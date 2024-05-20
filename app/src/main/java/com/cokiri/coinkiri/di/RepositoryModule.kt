package com.cokiri.coinkiri.di

import com.cokiri.coinkiri.data.remote.PreferencesManager
import com.cokiri.coinkiri.data.remote.api.AuthApi
import com.cokiri.coinkiri.data.remote.model.UpbitTickerResponse
import com.cokiri.coinkiri.data.repository.CoinRepositoryImpl
import com.cokiri.coinkiri.data.repository.KakaoLoginRepositoryImpl
import com.cokiri.coinkiri.data.repository.UserRepositoryImpl
import com.cokiri.coinkiri.data.repository.WebSocketRepositoryImpl
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.domain.repository.CoinRepository
import com.cokiri.coinkiri.domain.repository.KakaoLoginRepository
import com.cokiri.coinkiri.domain.repository.UserRepository
import com.cokiri.coinkiri.domain.repository.WebSocketRepository
import com.cokiri.coinkiri.presentation.price.UpbitWebSocketCallback
import com.cokiri.coinkiri.util.JsonParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideKakaoLoginRepository(kakaoLoginRepositoryImpl: KakaoLoginRepositoryImpl): KakaoLoginRepository{
        return kakaoLoginRepositoryImpl
    }


    @Provides
    @Singleton
    fun provideCoinRepository(coinRepositoryImpl: CoinRepositoryImpl): CoinRepository {
        return coinRepositoryImpl
    }


    @Provides
    @Singleton
    fun provideUserRepository(authApi: AuthApi, preferencesManager: PreferencesManager): UserRepository {
        return UserRepositoryImpl(authApi, preferencesManager)
    }


    @Provides
    @Singleton
    fun provideWebSocketRepository(webSocketRepositoryImpl: WebSocketRepositoryImpl): WebSocketRepository{
        return webSocketRepositoryImpl
    }


    @Provides
    @Singleton
    fun provideUpbitWebSocketCallback(): UpbitWebSocketCallback {
        return object : UpbitWebSocketCallback {
            override fun onUpbitTickerResponseReceived(ticker: Ticker) {
            }
        }
    }
}