package com.cokiri.coinkiri.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * accessToken이 있는 경우 헤더에 추가하는 Interceptor
 */

@Singleton
class AuthInterceptor @Inject constructor(
    // SharedPreferences를 사용하기 위한 PreferencesManager 주입
    private val preferencesManager: PreferencesManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()                                                    // 요청 객체(서버로 보낼 요청) 가져오기
        val accessToken = preferencesManager.getAccessToken()                                    // 액세스 토큰 가져오기(SharedPreferences에 저장된 액세스 토큰)
        val authRequired = originalRequest.header("AuthRequired")?.toBoolean() ?: false    // 요청 헤더에 AuthRequired가 있는지 확인하고 값이 true인지 확인

        // 액세스 토큰이 있고 인증이 필요한 경우
        return if (authRequired && accessToken != null) {
            val authenticatedRequest = originalRequest                           // 요청 객체(originalRequest)를 복사하여 authenticatedRequest에 저장
                .newBuilder()                                                    // 요청 객체를 수정하기 위한 빌더 객체 반환
                .removeHeader("AuthRequired")                              // 요청 보내기 전에 커스텀 헤더 제거
                .header("Authorization", "Bearer $accessToken")    // 헤더에 액세스 토큰 추가
                .build()                                                         // 수정된 요청 객체를 서버로 보내고 응답을 반환

            // 수정된 요청 객체를 서버로 보내고 응답을 반환
            chain.proceed(authenticatedRequest)
        } else {
            // 액세스 토큰이 없거나 인증이 필요하지 않은 경우 요청 객체를 그대로 서버로 보내고 응답을 반환
            chain.proceed(originalRequest)
        }
    }
}