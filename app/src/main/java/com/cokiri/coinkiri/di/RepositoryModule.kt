package com.cokiri.coinkiri.di

import com.cokiri.coinkiri.data.local.database.AppDatabase
import com.cokiri.coinkiri.data.remote.api.AnalysisApi
import com.cokiri.coinkiri.data.remote.service.preferences.PreferencesManager
import com.cokiri.coinkiri.data.remote.api.AuthApi
import com.cokiri.coinkiri.data.remote.api.CoinApi
import com.cokiri.coinkiri.data.remote.api.CommentApi
import com.cokiri.coinkiri.data.remote.api.LikeApi
import com.cokiri.coinkiri.data.remote.api.PostApi
import com.cokiri.coinkiri.data.repository.AnalysisRepositoryImpl
import com.cokiri.coinkiri.data.repository.CoinRepositoryImpl
import com.cokiri.coinkiri.data.repository.CommentRepositoryImpl
import com.cokiri.coinkiri.data.repository.KakaoLoginRepositoryImpl
import com.cokiri.coinkiri.data.repository.LikeRepositoryImpl
import com.cokiri.coinkiri.data.repository.PostRepositoryImpl
import com.cokiri.coinkiri.data.repository.UserRepositoryImpl
import com.cokiri.coinkiri.domain.repository.AnalysisRepository
import com.cokiri.coinkiri.domain.repository.CoinRepository
import com.cokiri.coinkiri.domain.repository.CommentRepository
import com.cokiri.coinkiri.domain.repository.KakaoLoginRepository
import com.cokiri.coinkiri.domain.repository.LikeRepository
import com.cokiri.coinkiri.domain.repository.PostRepository
import com.cokiri.coinkiri.domain.repository.UserRepository
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
    fun provideCoinRepository(
        coinApi: CoinApi,
        preferencesManager: PreferencesManager
    ): CoinRepository {
        return CoinRepositoryImpl(coinApi, preferencesManager)
    }


    /**
     * UserRepository를 제공하는 Provider 메소드
     * UserRepositoryImpl 클래스가 UserRepository 인터페이스에 주입되어 반환된다.
     * UserRepositoryImpl 클래스는 AuthApi와 PreferencesManager를 주입받아 생성자를 통해 의존성 주입을 받는다.
     */
    @Provides
    @Singleton
    fun provideUserRepository(
        authApi: AuthApi,
        preferencesManager: PreferencesManager,
        appDatabase: AppDatabase
    ): UserRepository {
        return UserRepositoryImpl(authApi, preferencesManager, appDatabase)
    }


    /**
     * CommentRepository를 제공하는 Provider 메소드
     * CommentRepositoryImpl 클래스가 CommentRepository 인터페이스에 주입되어 반환된다.
     */
    @Provides
    @Singleton
    fun provideCommentRepository(
        commentApi: CommentApi,
        preferencesManager: PreferencesManager,
        ): CommentRepository {
        return CommentRepositoryImpl(commentApi, preferencesManager)
    }


    @Provides
    @Singleton
    fun providePostRepository(
        postApi: PostApi,
        preferencesManager: PreferencesManager
    ): PostRepository {
        return PostRepositoryImpl(postApi,preferencesManager)
    }


    @Provides
    @Singleton
    fun provideAnalysisRepository(
        analysisApi: AnalysisApi,
        preferencesManager: PreferencesManager
    ): AnalysisRepository {
        return AnalysisRepositoryImpl(analysisApi, preferencesManager)
    }


    @Provides
    @Singleton
    fun provideLikeRepository(
        likeApi: LikeApi,
        preferencesManager: PreferencesManager
    ): LikeRepository {
        return LikeRepositoryImpl(likeApi, preferencesManager)
    }
}