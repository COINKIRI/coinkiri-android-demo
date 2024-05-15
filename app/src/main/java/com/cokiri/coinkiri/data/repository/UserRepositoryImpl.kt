package com.cokiri.coinkiri.data.repository

import android.util.Log
import com.cokiri.coinkiri.data.remote.api.SignUpApi
import com.cokiri.coinkiri.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 회원가입을 위한 UserRepository 구현체
 */
class UserRepositoryImpl @Inject constructor(
    private val signUpApi: SignUpApi
) : UserRepository {

    companion object { private const val TAG = "UserRepositoryImpl" }

    override suspend fun signUpUser(
        accessToken: String
    ): Result<Unit> {
        return try {
            signUpApi.signUpUser(accessToken)             // accessToken을 서버로 전송하여 회원가입 요청
            Log.i(TAG, "회원가입 성공")
            Result.success(Unit)
        } catch (error : Exception) {
            Log.e(TAG, "회원가입 실패", error)
            Result.failure(error)
        }
    }
}