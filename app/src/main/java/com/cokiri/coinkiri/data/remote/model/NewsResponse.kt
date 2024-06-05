package com.cokiri.coinkiri.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsResponse(
    val code: String,
    val message: String,
    val result: List<NewsList>
)

@JsonClass(generateAdapter = true)
data class NewsList(
    val id: Long,
    val title: String,
    val link: String,
    val description: String,
    val pubDate: String
)