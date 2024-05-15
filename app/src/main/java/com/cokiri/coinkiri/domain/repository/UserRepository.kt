package com.cokiri.coinkiri.domain.repository

/**
 * 회원가입을 위한 UserRepository Interface
 */
interface UserRepository {
    suspend fun signUpUser(
        accessToken: String
    ): Result<Unit>
}