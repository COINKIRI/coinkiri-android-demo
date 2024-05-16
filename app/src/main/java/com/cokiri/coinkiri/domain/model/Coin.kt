package com.cokiri.coinkiri.domain.model

data class Coin(
    val id: Int,
    val market: String,
    val koreanName: String,
    val englishName: String,
    val symbolImage: String
)