package com.cokiri.coinkiri.data.remote.model

data class ApiResponse(
    val code: String,
    val message: String,
    val result: Any?
)