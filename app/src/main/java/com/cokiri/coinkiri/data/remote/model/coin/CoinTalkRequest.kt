package com.cokiri.coinkiri.data.remote.model.coin

import com.squareup.moshi.JsonClass

/**
 * 코인톡 작성 요청
 */
@JsonClass(generateAdapter = true)
data class CoinTalkRequest(
    val coinId: Long,       // 댓글이 달릴 코인의 고유 ID
    val content: String     // 댓글의 내용
)