package com.cokiri.coinkiri.data.remote.model.auth

import com.squareup.moshi.JsonClass

/**
 * 회원 정보 조회 API 응답
 */
@JsonClass(generateAdapter = true)
data class MemberResponse(
    val code: String,
    val message: String,
    val result: MemberInfo   // 회원 정보
)

/**
 * 회원 정보
 */
@JsonClass(generateAdapter = true)
data class MemberInfo(
    val id: Long,                    // 회원의 고유 ID
    val nickname: String,            // 회원의 닉네임
    val exp: Int,                    // 회원의 경험치
    val level: Int,                  // 회원의 레벨
    val mileage: Int,                // 회원의 마일리지
    val pic: String?,                // 회원의 프로필 사진 URL (nullable)
    val statusMessage: String,       // 회원의 상태 메시지
    val followingCount: Int,         // 팔로잉 수
    val followerCount: Int           // 팔로워 수
)
