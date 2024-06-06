package com.cokiri.coinkiri.data.remote.model.post.news

import com.squareup.moshi.JsonClass

/**
 * 뉴스 리스트 응답
 */
@JsonClass(generateAdapter = true)
data class NewsListResponse(
    val code: String,
    val message: String,
    val result: List<NewsList>   // 뉴스 정보 목록
)

/**
 * 뉴스 정보
 */
@JsonClass(generateAdapter = true)
data class NewsList(
    val id: Long,               // 뉴스의 고유 ID
    val title: String,          // 뉴스 제목
    val link: String,           // 뉴스 링크
    val description: String,    // 뉴스 설명
    val pubDate: String         // 뉴스 발행일
)