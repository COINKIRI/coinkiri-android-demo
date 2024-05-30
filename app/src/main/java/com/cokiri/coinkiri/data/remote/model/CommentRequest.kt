package com.cokiri.coinkiri.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentRequest(
    val postId: Long,
    val content: String
)
