package com.cokiri.coinkiri.data

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * accessToken이 있는 경우 헤더에 추가하는 Interceptor
 */

@Singleton
class AuthInterceptor @Inject constructor(
    private val preferencesManager: PreferencesManager              // SharedPreferences를 사용하기 위한 PreferencesManager 주입
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()                      // 요청 객체(서버로 보낼 요청) 가져오기
        val accessToken = preferencesManager.getAccessToken()      // 액세스 토큰 가져오기(SharedPreferences에 저장된 액세스 토큰)

        return if (accessToken != null) {                                       // 액세스 토큰이 있을 경우
            val authenticatedRequest = originalRequest                          // 요청 객체(originalRequest)를 복사하여 authenticatedRequest에 저장
                .newBuilder()                                                   // 요청 객체를 수정하기 위한 빌더 객체 반환
                .header("Authorization", "Bearer $accessToken")   // 헤더에 액세스 토큰 추가
                .build()                                                        // 수정된 요청 객체 반환

            chain.proceed(authenticatedRequest)                                 // 수정된 요청 객체를 서버로 보내고 응답을 반환
        } else {
            chain.proceed(originalRequest)                                      // 액세스 토큰이 없을 경우 요청 객체를 그대로 서버로 보내고 응답을 반환
        }
    }
}
