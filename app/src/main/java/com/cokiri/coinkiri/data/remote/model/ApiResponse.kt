package com.cokiri.coinkiri.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse(
    val code: String,
    val message: String,
    val result: Any?
)