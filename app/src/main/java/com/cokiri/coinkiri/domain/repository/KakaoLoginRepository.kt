package com.cokiri.coinkiri.domain.repository

interface KakaoLoginRepository {
    fun login(
        successCallback: () -> Unit,
        failureCallback: () -> Unit
    )
    suspend fun logout()
}