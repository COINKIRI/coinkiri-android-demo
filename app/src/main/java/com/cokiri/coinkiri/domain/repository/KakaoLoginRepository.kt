package com.cokiri.coinkiri.domain.repository

/**
 * 카카오 로그인을 위한 KakaoLoginRepository interface
 */
interface KakaoLoginRepository {

    // 로그인
    fun login(
        successCallback: (String) -> Unit,
        failureCallback: () -> Unit
    )

    // 로그아웃
    suspend fun logout()
}