package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.auth.UpdateMemberInfoRequest


/**
 * 회원가입을 위한 UserRepository Interface
 */
interface UserRepository {

    suspend fun signUpUser(accessToken: String, socialType: String): Result<Unit>

    suspend fun logoutUser(): Result<Unit>

    suspend fun getMemberInfo(): MemberInfoEntity?

    suspend fun updateMemberInfo(updateMemberInfoRequest: UpdateMemberInfoRequest): ApiResponse

    fun isLoggedIn(): Boolean

}