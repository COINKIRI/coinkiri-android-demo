package com.cokiri.coinkiri.data.remote.model.coin

import com.squareup.moshi.JsonClass

/**
 * 관심 코인 조회 응답
 */
@JsonClass(generateAdapter = true)
data class WatchlistResponse(
    val code: String,
    val message: String,
    val result: WatchlistItem
)

@JsonClass(generateAdapter = true)
data class WatchlistItem(
    val coinPrices: List<WatchlistCoinPrice>
)

@JsonClass(generateAdapter = true)
data class WatchlistCoinPrice(
    val coinId: Long,
    val market: String,
    val koreanName: String,
    val symbolImage: String,
    val coinPrices: List<WatchlistPrice>
){
    // krw- 제거
    val marketName: String
        get() = market.replace("KRW-", "")
}

@JsonClass(generateAdapter = true)
data class WatchlistPrice(
    val candleDateTimeKst: String,
    val tradePrice: Long
)
