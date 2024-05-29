package com.cokiri.coinkiri.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentResponse(
    val code: String,
    val message: String,
    val result: List<CommentList>,
)

@JsonClass(generateAdapter = true)
data class CommentList(
    val id: Long,
    val content: String,
    val createdAt: String,
    val modifiedAt: String,
    val member: Member
)


@JsonClass(generateAdapter = true)
data class Member(
    val id: Long,
    val nickname: String,
    val level: Int,
    val pic: String
)
