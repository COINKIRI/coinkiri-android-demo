package com.cokiri.coinkiri.data.repository

import android.util.Log
import com.cokiri.coinkiri.data.remote.api.LikeApi
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisResponseDto
import com.cokiri.coinkiri.data.remote.model.post.community.CommunityResponseDto
import com.cokiri.coinkiri.data.remote.service.preferences.PreferencesManager
import com.cokiri.coinkiri.domain.repository.LikeRepository
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val likeApi: LikeApi,
    private val preferencesManager: PreferencesManager
) : LikeRepository {

    private var cachedLikeCommunityList : List<CommunityResponseDto>? = null // 캐시된 좋아요한 커뮤니티 게시물 목록
    private var cachedLikeAnalysisList : List<AnalysisResponseDto>? = null // 캐시된 좋아요한 분석글 목록
    private var lastFetchTimeLikeCommunityList : Long = 0 // 마지막으로 좋아요한 커뮤니티 게시물 목록을 가져온 시간
    private var lastFetchTimeLikeAnalysisList : Long = 0 // 마지막으로 좋아요한 분석글 목록을 가져온 시간

    companion object {
        private const val CACHE_VALIDITY_DURATION_LIKE_COMMUNITY = 5 * 60 * 1000 // 캐시 유효 기간 (5분)
        private const val CACHE_VALIDITY_DURATION_LIKE_ANALYSIS = 5 * 60 * 1000 // 캐시 유효 기간 (5분)
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


    /**
     * 좋아요 한 게시물 리스트 조회
     */
    override suspend fun fetchLikeCommunityList(forceRefresh: Boolean) : List<CommunityResponseDto> {
        val accessToken = preferencesManager.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            throw AuthException("로그인이 필요합니다.")
        }

        val currentTime = System.currentTimeMillis()
        return if (forceRefresh || cachedLikeCommunityList.isNullOrEmpty() || currentTime - lastFetchTimeLikeCommunityList > CACHE_VALIDITY_DURATION_LIKE_COMMUNITY) {
            val response = likeApi.fetchLikeCommunityList("Bearer $accessToken")
            cachedLikeCommunityList = response.result
            lastFetchTimeLikeCommunityList = currentTime
            cachedLikeCommunityList!!
        } else {
            cachedLikeCommunityList!!
        }

    }


    /**
     * 좋아요 한 분석글 리스트 조회
     */
    override suspend fun fetchLikeAnalysisList(forceRefresh: Boolean) : List<AnalysisResponseDto> {
        val accessToken = preferencesManager.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            throw AuthException("로그인이 필요합니다.")
        }

        val currentTime = System.currentTimeMillis()
        return if (forceRefresh || cachedLikeAnalysisList.isNullOrEmpty() || currentTime - lastFetchTimeLikeAnalysisList > CACHE_VALIDITY_DURATION_LIKE_ANALYSIS) {
            val response = likeApi.fetchLikeAnalysisList("Bearer $accessToken")
            cachedLikeAnalysisList = response.result
            lastFetchTimeLikeAnalysisList = currentTime
            cachedLikeAnalysisList!!
        } else {
            cachedLikeAnalysisList!!
        }

    }
}