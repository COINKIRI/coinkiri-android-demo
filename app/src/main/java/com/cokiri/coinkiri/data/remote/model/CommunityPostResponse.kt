package com.cokiri.coinkiri.data.remote.model

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@JsonClass(generateAdapter = true)
data class CommunityResponse(
    val code: String,
    val message: String,
    val result: List<CommunityResponseDto>
)

data class CommunityResponseDto(
    val postResponseDto : PostResponseDto,
    val category : String
)


@JsonClass(generateAdapter = true)
data class PostResponseDto(
    val id: Long,
    val title: String,
    val viewCnt: Int,
    val createdAt : String,
    val memberNickname: String,
    val memberLevel: Int,
    val commentCount: Int,
    val likeCount: Int
) {
    val formattedDateTime: String
        get() {
            val dateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        }

    val formattedDate: String
        get() {
            val dateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH:mm"))
        }
}