package com.cokiri.coinkiri.data.repository

import android.util.Log
import com.cokiri.coinkiri.data.local.database.AppDatabase
import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity
import com.cokiri.coinkiri.data.remote.service.preferences.PreferencesManager
import com.cokiri.coinkiri.data.remote.api.AuthApi
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.auth.MemberInfo
import com.cokiri.coinkiri.data.remote.model.auth.SignUpRequest
import com.cokiri.coinkiri.data.remote.model.auth.UpdateMemberInfoRequest
import com.cokiri.coinkiri.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 회원가입을 위한 UserRepository 구현체
 */
class UserRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val preferencesManager: PreferencesManager,
    private val appDatabase: AppDatabase
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }

    override suspend fun signUpUser(accessToken: String, socialType: String): Result<Unit> {
        return try {
            val response = authApi.signUpUser(SignUpRequest(accessToken, socialType))
            val accessToken = response.result.accessToken
            val refreshToken = response.result.refreshToken

            preferencesManager.saveAccessToken(accessToken)
            preferencesManager.saveRefreshToken(refreshToken)

            Log.i(TAG, "회원가입 성공, accessToken : $accessToken, refreshToken : $refreshToken")

            getMemberInfo()?.let { member ->
                Log.d(TAG, "사용자 정보: $member")
            }

            Result.success(Unit)
        } catch (error: Exception) {
            Log.e(TAG, "회원가입 실패", error)
            Result.failure(error)
        }
    }


    override suspend fun logoutUser(): Result<Unit> {
        return try {
            val accessToken = preferencesManager.getAccessToken()
            if (accessToken.isNullOrEmpty()) {
                Log.e(TAG, "로그아웃 실패: 토큰이 없음")
                Result.failure(Exception("토큰이 없음"))
            } else {
                Log.d(TAG, "로그아웃 요청 토큰: $accessToken")
                authApi.logoutUser("Bearer $accessToken")
                preferencesManager.clearTokens()
                deleteMemberInfo()
                Log.i(TAG, "로그아웃 성공")
                Result.success(Unit)
            }
        } catch (error: Exception) {
            Log.e(TAG, "로그아웃 실패", error)
            Result.failure(error)
        }
    }


    override suspend fun getMemberInfo(): MemberInfoEntity? {
        return try {
            val accessToken = preferencesManager.getAccessToken()
            if (accessToken.isNullOrEmpty()) {
                Log.e(TAG, "토큰이 없어 사용자 정보를 가져올 수 없습니다.")
                null
            } else {
                val response = authApi.getMemberInfo("Bearer $accessToken")
                if (response.code == "OK") {
                    val result = response.result
                    val memberInfoEntity = result.toEntity()
                    insertMemberInfo(memberInfoEntity)
                    memberInfoEntity
                } else {
                    Log.e(TAG, "서버 응답 오류: ${response.message}")
                    null
                }
            }
        } catch (error: Exception) {
            Log.e(TAG, "사용자 정보 가져오기 실패", error)
            null
        }
    }


    override suspend fun updateMemberInfo(updateMemberInfoRequest: UpdateMemberInfoRequest) : ApiResponse {
        // 액세스 토큰을 preferencesManager에서 가져옴
        val accessToken = preferencesManager.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            throw AuthException("로그인이 필요합니다.")
        }

        // authApi를 사용하여 사용자 정보 수정 요청
        val response = authApi.updateMemberInfo("Bearer $accessToken", updateMemberInfoRequest)
        Log.d(TAG, "updateMemberInfo: $response")

        if (response.isSuccessful) {
            // 사용자 정보 수정 성공
            // 사용자 정보 업데이트
            //val memberInfoEntity = response.result.toEntity()
            //insertMemberInfo(memberInfoEntity)
            // 응답 데이터 반환
            return response.body() ?: throw ApiException("응답 데이터가 null입니다.")
        } else {
            throw ApiException("응답이 실패하였습니다.")
        }
    }




    override fun isLoggedIn(): Boolean {
        return !preferencesManager.getAccessToken().isNullOrEmpty()
    }

    private suspend fun insertMemberInfo(memberInfoEntity: MemberInfoEntity) {
        appDatabase.memberInfoDao().insertMemberInfo(memberInfoEntity)
    }

    private suspend fun deleteMemberInfo() {
        appDatabase.memberInfoDao().deleteAll()
    }

    private fun MemberInfo.toEntity() = MemberInfoEntity(
        id = id,
        nickname = nickname,
        exp = exp,
        level = level,
        mileage = mileage,
        pic = pic,
        statusMessage = statusMessage,
        followingCount = followingCount,
        followerCount = followerCount
    )


    class AuthException(message: String) : Exception(message)  // 로그인 관련 예외 클래스
    class ApiException(message: String) : Exception(message)   // API 요청 관련 예외 클래스
}