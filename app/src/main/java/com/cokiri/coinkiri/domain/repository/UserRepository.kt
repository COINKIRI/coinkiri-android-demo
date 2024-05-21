package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity


/**
 * 회원가입을 위한 UserRepository Interface
 */
interface UserRepository {
    suspend fun signUpUser(accessToken: String, socialType: String): Result<Unit>
    suspend fun logoutUser(): Result<Unit>
    suspend fun getMemberInfo(): MemberInfoEntity?
    fun isLoggedIn(): Boolean
    suspend fun updateMemberInfo(memberInfo: MemberInfoEntity)
}