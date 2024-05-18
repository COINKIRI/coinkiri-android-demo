package com.cokiri.coinkiri.data.remote.model

/**
 * 회원가입 응답 모델
 * @param Result : 회원가입 결과
 */
data class SignUpResponse(
    val result: Result
)


/**
 * 회원가입 결과 모델
 * @param accessToken  : 액세스 토큰
 * @param refreshToken : 리프레시 토큰
 */
data class Result(
    val accessToken: String,
    val refreshToken: String
)