package com.cokiri.coinkiri.data.repository

import android.util.Log
import com.cokiri.coinkiri.data.remote.api.AnalysisApi
import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisPostDataRequest
import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisResponseDto
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.service.preferences.PreferencesManager
import com.cokiri.coinkiri.domain.repository.AnalysisRepository
import javax.inject.Inject

class AnalysisRepositoryImpl @Inject constructor(
    private val analysisApi: AnalysisApi,
    private val preferencesManager: PreferencesManager
) : AnalysisRepository {

    private var cachedAnalysisPostList : List<AnalysisResponseDto>? = null     // 캐시된 분석 게시물 목록
    private var cachedUserAnalysisPostList : List<AnalysisResponseDto>? = null
    private var lastFetchTimeAnalysis: Long = 0                                // 마지막으로 분석 게시물 목록을 가져온 시간
    private var lastFetchTimeUserAnalysis: Long = 0


    companion object {
        private const val CACHE_VALIDITY_DURATION_ANALYSIS = 5 * 60 * 1000     // 캐시 유효 기간 (5분)
        private const val CACHE_VALIDITY_DURATION_USER_ANALYSIS = 5 * 60 * 1000     // 캐시 유효 기간 (5분)
        private const val TAG = "AnalysisRepositoryImpl"                       // 로그 태그
    }


    /**
     * 분석글 작성 요청 (POST)
     */
    override suspend fun submitAnalysisPost(analysisPostDataRequest: AnalysisPostDataRequest): ApiResponse {
        // 액세스 토큰을 preferencesManager에서 가져옴
        val accessToken = preferencesManager.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            throw AuthException("로그인이 필요합니다.")
        }

        // PostApi를 사용하여 분석글 작성 요청
        val response = analysisApi.submitAnalysisPost("Bearer $accessToken", analysisPostDataRequest)
        Log.d(TAG, "submitAnalysisPost: $response")

        if (response.isSuccessful) {
            // 캐시 무효화
            clearCache()
            // 응답 데이터 반환, null인 경우 예외 발생
            return response.body() ?: throw PostRepositoryImpl.ApiException("응답 데이터가 null입니다.")
        } else {
            throw ApiException("응답이 실패하였습니다.")
        }
    }


    /**
     * 전체 분석글 목록 요청 (GET)
     */
    override suspend fun getAnalysisPostList(forceRefresh: Boolean): List<AnalysisResponseDto> {
        // 현재 시간 가져오기
        val currentTime = System.currentTimeMillis()
        return if (forceRefresh || cachedAnalysisPostList.isNullOrEmpty() || currentTime - lastFetchTimeAnalysis > CACHE_VALIDITY_DURATION_ANALYSIS) {
            // 강제 새로 고침이 필요하거나 캐시가 비어있거나 캐시 유효 기간이 지난 경우
            // PostApi를 사용하여 전체 분석글 목록 요청
            val response = analysisApi.getAllAnalysisPost()
            // 응답 결과를 캐시에 저장
            cachedAnalysisPostList = response.result
            lastFetchTimeAnalysis = currentTime
            // 캐시된 분석글 목록 반환
            cachedAnalysisPostList!!
        } else {
            // 유효한 캐시가 있는 경우 캐시된 목록 반환
            cachedAnalysisPostList!!
        }
    }


    /**
     *  작성한 분석글 목록 요청 (GET)
     */
    override suspend fun fetchUserAnalysisList(forceRefresh: Boolean) : List<AnalysisResponseDto> {
        val accessToken = preferencesManager.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            throw LikeRepositoryImpl.AuthException("로그인이 필요합니다.")
        }

        val currentTime = System.currentTimeMillis()
        return if (forceRefresh || cachedUserAnalysisPostList.isNullOrEmpty() || currentTime - lastFetchTimeUserAnalysis > CACHE_VALIDITY_DURATION_USER_ANALYSIS) {
            val response = analysisApi.fetchUserAnalysisList("Bearer $accessToken")
            cachedUserAnalysisPostList = response.result
            lastFetchTimeUserAnalysis = currentTime
            cachedUserAnalysisPostList!!
        } else {
            cachedUserAnalysisPostList!!
        }
    }


    /**
     * 분석글 상세 요청 (GET)
     * @param postId 요청할 글의 ID
     * @return AnalysisDetailResponseDto 글 상세 정보
     * @throws Exception 요청 중 발생한 예외
     */
    override suspend fun getAnalysisPostDetail(postId: Long) : AnalysisDetailResponseDto {
        return try {
            // PostApi를 사용하여 분석글 상세 요청
            val response = analysisApi.getAnalysisPostDetail(postId)
            response.result
        } catch (e: Exception) {
            throw e
        }
    }


    /**
     * 캐시 무효화
     * 캐시된 분석글 목록을 초기화
     * 마지막으로 분석글 목록을 가져온 시간을 0으로 초기화
     */
    private fun clearCache() {
        cachedAnalysisPostList = null
        cachedUserAnalysisPostList = null
        lastFetchTimeAnalysis = 0
        lastFetchTimeUserAnalysis = 0
    }

    class AuthException(message: String) : Exception(message)  // 로그인 관련 예외 클래스
    class ApiException(message: String) : Exception(message)   // API 요청 관련 예외 클래스
}