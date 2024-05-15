package com.cokiri.coinkiri.data.remote.model

data class LoginRequest(
    val token: String,
    val socialType: String
)
