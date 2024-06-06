package com.cokiri.coinkiri.data.remote.model.auth

import com.squareup.moshi.JsonClass

/**
 * 회원가입 요청
 */
@JsonClass(generateAdapter = true)
data class SignUpRequest(
    val token: String,          // 소셜 로그인 인증 토큰
    val socialType: String      // 소셜 로그인 유형 (예: "kakao", "naver")
)