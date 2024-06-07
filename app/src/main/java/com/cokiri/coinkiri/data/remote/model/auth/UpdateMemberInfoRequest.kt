package com.cokiri.coinkiri.data.remote.model.auth

import com.squareup.moshi.JsonClass

/**
 * 회원 정보 수정 요청
 */
@JsonClass(generateAdapter = true)
data class UpdateMemberInfoRequest(
    val nickname: String,
    val statusMessage: String
)
