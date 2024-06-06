package com.cokiri.coinkiri.data.remote.model.auth

/**
 * 회원가입 응답
 */
data class SignUpResponse(
    val code: String,
    val message: String,
    val result: SignUpResult    // 회원가입 결과
)

/**
 * 회원가입 결과
 */
data class SignUpResult(
    val accessToken: String,    // 액세스 토큰
    val refreshToken: String    // 리프레시 토큰
)