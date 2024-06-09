package com.cokiri.coinkiri.data.remote.model.comment

import com.squareup.moshi.JsonClass

/**
 * 댓글 조회 API 응답
 */
@JsonClass(generateAdapter = true)
data class CommentResponse(
    val code: String,
    val message: String,
    val result: List<CommentList>   // 댓글 목록
)

/**
 * 댓글 정보
 */
@JsonClass(generateAdapter = true)
data class CommentList(
    val id: Long,                   // 댓글의 고유 ID
    val content: String,            // 댓글의 내용
    val createdAt: String,          // 댓글 생성 날짜 및 시간
    val modifiedAt: String,         // 댓글 수정 날짜 및 시간
    val member: Member              // 댓글 작성자 정보
)

/**
 * 댓글 작성자 정보
 */
@JsonClass(generateAdapter = true)
data class Member(
    val id: Long,                   // 작성자의 고유 ID
    val nickname: String,           // 작성자의 닉네임
    val level: Int,                 // 작성자의 레벨
    val pic: String                 // 작성자의 프로필 사진 URL
)