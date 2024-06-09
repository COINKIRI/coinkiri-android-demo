package com.cokiri.coinkiri.data.remote.model.coin

import com.squareup.moshi.JsonClass

/**
 * 코인톡 리스트 응답
 */
@JsonClass(generateAdapter = true)
data class CoinTalkResponse(
    val code: String,
    val message: String,
    val result: List<CoinTalk>   // 코인톡 정보 목록
)

/**
 * 코인톡 정보
 */
@JsonClass(generateAdapter = true)
data class CoinTalk(
    val content: String,          // 코인톡 내용
    val createdAt: String,        // 코인톡 작성일
    val member: Member            // 작성자 정보
)

/**
 * 작성자 정보
 */
@JsonClass(generateAdapter = true)
data class Member(
    val id: Long,               // 작성자의 고유 ID
    val nickname: String,       // 작성자의 닉네임
    val level: Int,             // 작성자의 레벨
    val pic: String             // 작성자의 프로필 사진 URL
)