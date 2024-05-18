package com.cokiri.coinkiri.domain.model

data class Coin(
    val coinId: Long,
    val market: String,
    val koreanName: String,
    val englishName: String,
    val symbolImage: String?
)