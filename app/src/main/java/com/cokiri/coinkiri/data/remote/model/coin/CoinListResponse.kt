package com.cokiri.coinkiri.data.remote.model.coin

import com.squareup.moshi.JsonClass

/**
 * 코인 리스트 응답
 */
@JsonClass(generateAdapter = true)
data class CoinListResponse(
    val code: String,
    val message: String,
    val result: List<CoinInfo>   // 코인 정보 목록
)

/**
 * 코인 정보
 */
@JsonClass(generateAdapter = true)
data class CoinInfo(
    val id: Long,                // 코인의 고유 ID
    val market: String,          // 코인이 거래되는 시장 (예: "KRW-BTC")
    val koreanName: String,      // 코인의 한국어 이름
    val englishName: String,     // 코인의 영어 이름
    val symbolImage: String?     // 코인 심볼 이미지 (nullable)
)
