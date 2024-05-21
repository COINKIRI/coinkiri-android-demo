package com.cokiri.coinkiri.di

import com.cokiri.coinkiri.data.remote.PreferencesManager
import com.cokiri.coinkiri.data.remote.api.AuthApi
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * KakaoLoginRepository를 제공하는 Provider 메소드
     * KakaoLoginRepositoryImpl 클래스가 KakaoLoginRepository 인터페이스에 주입되어 반환된다.
     */
    @Provides
    @Singleton
    fun provideKakaoLoginRepository(kakaoLoginRepositoryImpl: KakaoLoginRepositoryImpl): KakaoLoginRepository{
        return kakaoLoginRepositoryImpl
    }


    /**
     * CoinRepository를 제공하는 Provider 메소드
     * CoinRepositoryImpl 클래스가 CoinRepository 인터페이스에 주입되어 반환된다.
     */
    @Provides
    @Singleton
    fun provideCoinRepository(coinRepositoryImpl: CoinRepositoryImpl): CoinRepository {
        return coinRepositoryImpl
    }


    /**
     * UserRepository를 제공하는 Provider 메소드
     * UserRepositoryImpl 클래스가 UserRepository 인터페이스에 주입되어 반환된다.
     * UserRepositoryImpl 클래스는 AuthApi와 PreferencesManager를 주입받아 생성자를 통해 의존성 주입을 받는다.
     */
    @Provides
    @Singleton
    fun provideUserRepository(authApi: AuthApi, preferencesManager: PreferencesManager): UserRepository {
        return UserRepositoryImpl(authApi, preferencesManager)
    }


    /**
     * WebSocketRepository를 제공하는 Provider 메소드
     * WebSocketRepositoryImpl 클래스가 WebSocketRepository 인터페이스에 주입되어 반환된다.
     */
    @Provides
    @Singleton
    fun provideWebSocketRepository(webSocketRepositoryImpl: WebSocketRepositoryImpl): WebSocketRepository{
        return webSocketRepositoryImpl
    }


    /**
     * UpbitWebSocketCallback을 제공하는 Provider 메소드
     * UpbitWebSocketCallback 인터페이스를 구현한 객체를 반환한다.
     * UpbitWebSocketCallback 인터페이스를 구현한 객체는 UpbitWebSocketCallback을 구현한 객체를 반환한다.
     */
    @Provides
    @Singleton
    fun provideUpbitWebSocketCallback(): UpbitWebSocketCallback {
        return object : UpbitWebSocketCallback {
            override fun onUpbitTickerResponseReceived(ticker: Ticker) {
            }
        }
    }
}