package com.cokiri.coinkiri.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoinResponse(
    @Json(name = "code") val code: String,
    @Json(name = "message") val message: String,
    @Json(name = "result") val result: List<CoinInfo>
)

@JsonClass(generateAdapter = true)
data class CoinInfo(
    @Json(name = "id") val id: Int,
    @Json(name = "market") val market: String,
    @Json(name = "koreanName") val koreanName: String,
    @Json(name = "englishName") val englishName: String,
    @Json(name = "symbolImage") val symbolImage: String
)
