package com.cokiri.coinkiri.data.remote.model.analysis

import com.cokiri.coinkiri.data.remote.model.post.community.PostDetailResponseDto
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnalysisDetailResponse(
    val code: String,
    val message: String,
    val result: AnalysisDetailResponseDto
)


@JsonClass(generateAdapter = true)
data class AnalysisDetailResponseDto(
    val postDetailResponseDto: PostDetailResponseDto,
    val coin: Coin,
    val coinPrevClosingPrice: String,
    val investmentOption: String,
    val targetPrice: String,
    val targetPeriod: String
)