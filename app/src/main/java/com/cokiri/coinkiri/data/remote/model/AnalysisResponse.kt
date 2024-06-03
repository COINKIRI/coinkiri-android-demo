package com.cokiri.coinkiri.data.remote.model

data class AnalysisResponse(
    val code: String,
    val message: String,
    val result: List<AnalysisResponseDto>
)

data class AnalysisResponseDto(
    val postResponseDto : PostResponseDto,
    val coin : Coin,
    val coinPrevClosingPrice: String,
    val investmentOption: String,
    val targetPrice: String,
    val targetPeriod: String,
    val memberPic: String,
)


data class Coin(
    val id: Long,
    val market: String,
    val koreanName: String,
    val englishName: String,
    val symbolImage: String?
) {
    //market에 KRW 추가
    val krwMarket: String
        get() = "KRW-$market"

}