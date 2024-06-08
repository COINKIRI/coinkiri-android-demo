package com.cokiri.coinkiri.data.repository

import android.util.Log
import com.cokiri.coinkiri.data.remote.api.LikeApi
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.service.preferences.PreferencesManager
import com.cokiri.coinkiri.domain.repository.LikeRepository
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val likeApi: LikeApi,
    private val preferencesManager: PreferencesManager
) : LikeRepository {

    companion object {
        private const val TAG = "LikeRepositoryImpl"
    }

    class AuthException(message: String) : Exception(message)
    class ApiException(message: String) : Exception(message)

    /**
     * 좋아요를 누름
     */
    override suspend fun addLike(postId: Long): ApiResponse {
        val accessToken = preferencesManager.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            throw AuthException("로그인이 필요합니다.")
        }

        val response = likeApi.addLike("Bearer $accessToken", postId)
        if (response.isSuccessful) {
            // 응답 데이터 반환, null인 경우 예외 발생
            return response.body() ?: throw ApiException("응답 데이터가 null입니다.")
        } else {
            throw ApiException("응답이 실패하였습니다.")
        }
    }

    /**
     * 좋아요를 취소함
     */
    override suspend fun deleteLike(postId: Long): ApiResponse {
        val accessToken = preferencesManager.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            throw AuthException("로그인이 필요합니다.")
        }

        val response = likeApi.deleteLike("Bearer $accessToken", postId)
        if (response.isSuccessful) {
            // 응답 데이터 반환, null인 경우 예외 발생
            return response.body() ?: throw ApiException("응답 데이터가 null입니다.")
        } else {
            throw ApiException("응답이 실패하였습니다.")
        }
    }


    /**
     * 좋아요 여부 확인
     */
    override suspend fun checkLike(postId: Long): Boolean {
        val accessToken = preferencesManager.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            throw AuthException("로그인이 필요합니다.")
        }

        val response = likeApi.checkLike("Bearer $accessToken", postId)
        Log.d(TAG, "checkLike: $response")
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null && apiResponse.result is Boolean) {
                return apiResponse.result
            } else {
                throw ApiException("Invalid response format")
            }
        } else {
            throw ApiException("응답이 실패하였습니다.")
        }
    }
}