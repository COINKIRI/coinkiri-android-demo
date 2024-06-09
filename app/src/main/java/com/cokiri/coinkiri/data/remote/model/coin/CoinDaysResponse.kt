package com.cokiri.coinkiri.data.remote.model.coin

import com.squareup.moshi.JsonClass
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JsonClass(generateAdapter = true)
data class CoinDaysResponse(
    val code: String,
    val message: String,
    val result: CoinDaysInfoResult
)

@JsonClass(generateAdapter = true)
data class CoinDaysInfoResult(
    val market: String,
    val coinPrices: List<CoinPrice>
)

@JsonClass(generateAdapter = true)
data class CoinPrice(
    val candleDateTimeKst: String,
    val tradePrice: Double
) {
    val formattedDateTime: String
        get() {
            val dateTime = LocalDateTime.parse(candleDateTimeKst, DateTimeFormatter.ISO_DATE_TIME)
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
        }
    val formattedTradePrice: String
        get() = tradePrice.let { DecimalFormat("#,###").format(it) } ?: "0"
}