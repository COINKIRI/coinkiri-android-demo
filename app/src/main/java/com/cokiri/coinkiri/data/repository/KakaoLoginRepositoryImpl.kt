package com.cokiri.coinkiri.data.repository

import android.content.Context
import android.util.Log
import com.cokiri.coinkiri.domain.repository.KakaoLoginRepository
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 카카오 로그인을 위한 KakaoLoginRepository 구현체
 */

class KakaoLoginRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : KakaoLoginRepository {

    companion object { private const val TAG = "KakaoLoginRepositoryImpl" }

    // 카카오 로그인
    override fun login(
        successCallback: (String) -> Unit,  // 성공 시 호출할 콜백 (accessToken: String)을 인자로 받음
        failureCallback: () -> Unit         // 실패 시 호출할 콜백
    ) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
                failureCallback()
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                successCallback(token.accessToken)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        failureCallback()
                        return@loginWithKakaoTalk
                    }

                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    successCallback(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }


    // 카카오 로그아웃
    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                } else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
        }
    }
}