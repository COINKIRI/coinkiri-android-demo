package com.cokiri.coinkiri.data.remote.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class CommunityResponse(
    val code: String,
    val message: String,
    val result: List<CommunityResponseDto>
)

@JsonClass(generateAdapter = true)
data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val images: List<ImageData>,
    val viewCnt: Int,
    val createdAt: String,
    val modifiedAt: String,
    val member: Member,
    val comments: List<Comment>
)

data class CommunityResponseDto(
    val postResponseDto : PostResponseDto,
    val category : String
)

data class Member(
    val id : Long,
    val nickname : String,
    val exp : Int,
    val level : Int,
    val mileage : Int,
    val pic : String?,
    val statusMessage : String
)

@JsonClass(generateAdapter = true)
data class Comment(
    val id: Long,
    val content: String,
    val createdAt: String,
    val modifiedAt: String,
    val member: Member
)


