package com.cokiri.coinkiri.data.remote.model.analysis


data class AnalysisPostDataRequest(
    val postRequestDto: PostRequestDto,      // 게시글 내용
    val coinId: Long,                        // 코인 ID
    val coinPrevClosingPrice: String,        // 코인 전일 종가
    val investmentOption: String,            // 투자 옵션
    val targetPrice: String,                 // 목표 가격
    val targetPeriod: String                 // 목표 기간
)

data class PostRequestDto(
    val title: String,
    val content: String,
    val images: List<ImageData>
)

data class ImageData(
    val position: Int,
    val base64: String
)