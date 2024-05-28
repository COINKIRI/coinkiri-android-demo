package com.cokiri.coinkiri.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommunityDetailResponse(
    val code: String,
    val message: String,
    val result: CommunityDetailResponseDto,
)

@JsonClass(generateAdapter = true)
data class CommunityDetailResponseDto(
    val postDetailResponseDto: PostDetailResponseDto,
    val category : String
)

@JsonClass(generateAdapter = true)
data class PostDetailResponseDto(
    val title: String,
    val content: String,
    val viewCnt: Int,
    val createdAt: String,
    val memberNickname: String,
    val memberLevel: Int,
    val memberPic: String,
    val likeCount: Int,
    val images: List<ImageResponseDto>,
    val comments: List<CommentResponseDto>,
)


@JsonClass(generateAdapter = true)
data class ImageResponseDto(
    val position: Int,
    val base64: String
)

@JsonClass(generateAdapter = true)
data class CommentResponseDto(
    val content: String,
    val createdAt: String,
    val modifiedAt: String,
    val member: Member
)

@JsonClass(generateAdapter = true)
data class Member(
    val id : Long,
    val nickname : String,
    val level : Int,
    val pic : String
)
