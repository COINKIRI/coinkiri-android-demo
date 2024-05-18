package com.cokiri.coinkiri.data.remote.model

/**
 * 회원가입 요청 모델
 * @param token      : 소셜로그인 토큰
 * @param socialType : 소셜로그인 타입
 */
data class SignUpRequest(
    val token: String,
    val socialType: String
)