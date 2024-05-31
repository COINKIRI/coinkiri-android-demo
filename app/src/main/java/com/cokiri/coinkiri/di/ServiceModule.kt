package com.cokiri.coinkiri.di

import com.cokiri.coinkiri.data.remote.service.WebSocketServiceImpl
import com.cokiri.coinkiri.domain.service.WebSocketService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideWebSocketService(webSocketServiceImpl: WebSocketServiceImpl): WebSocketService {
        return webSocketServiceImpl
    }
}