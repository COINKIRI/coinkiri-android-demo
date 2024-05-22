package com.cokiri.coinkiri.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MemberResponse(
    @Json(name = "code") val code: String,
    @Json(name = "message") val message: String,
    @Json(name = "result") val result: MemberInfo
)

@JsonClass(generateAdapter = true)
data class MemberInfo(
    val id : Long,
    val nickname : String,
    val exp : Int,
    val level : Int,
    val mileage : Int,
    val pic : String?,
    val statusMessage : String,
    val followingCount : Int,
    val followerCount : Int
)