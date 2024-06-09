package com.cokiri.coinkiri.data.repository

import android.util.Log
import com.cokiri.coinkiri.data.remote.api.PostApi
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.post.community.CommunityDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.post.community.CommunityResponseDto
import com.cokiri.coinkiri.data.remote.model.post.news.NewsList
import com.cokiri.coinkiri.data.remote.model.analysis.PostRequestDto
import com.cokiri.coinkiri.data.remote.service.preferences.PreferencesManager
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postApi: PostApi,
    private val preferencesManager: PreferencesManager
) : PostRepository {

    private var cachedCommunityPostList : List<CommunityResponseDto>? = null     // 캐시된 커뮤니티 게시물 목록
    private var cachedNewsList : List<NewsList>? = null                          // 캐시된 뉴스 목록
    private var lastFetchTimeCommunity: Long = 0                                 // 마지막으로 게시물 목록을 가져온 시간
    private var lastNewsFetchTime: Long = 0                                      // 마지막으로 뉴스 목록을 가져온 시간


    companion object {
        private const val CACHE_VALIDITY_DURATION_COMMUNITY = 5 * 60 * 1000   // 캐시 유효 기간 (5분)
        private const val CACHE_VALIDITY_DURATION_NEWS = 5 * 60 * 1000        // 캐시 유효 기간 (5분)
        private const val TAG = "PostRepositoryImpl"                          // 로그 태그
    }


    /**
     * 게시글 작성 요청 (POST)
     * @param postRequestDto 작성할 글의 데이터
     * @return ApiResponse 작성 결과 응답
     * @throws AuthException 로그인이 필요할 때 발생
     * @throws ApiException 응답이 실패하거나 데이터가 null일 때 발생
     */
    override suspend fun submitPost(postRequestDto: PostRequestDto): ApiResponse {
        // 액세스 토큰을 preferencesManager에서 가져옴
        val accessToken = preferencesManager.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            throw AuthException("로그인이 필요합니다.")
        }

        // PostApi를 사용하여 글 작성 요청
        val response = postApi.submitPost("Bearer $accessToken", postRequestDto)
        Log.d(TAG, "submitPost: $response")

        if (response.isSuccessful) {
            // 캐시 무효화
            clearCache()
            // 응답 데이터 반환, null인 경우 예외 발생
            return response.body() ?: throw ApiException("응답 데이터가 null입니다.")
        } else {
            throw ApiException("응답이 실패하였습니다.")
        }
    }


    /**
     * 전체 커뮤니티 글 목록 요청 (GET)
     * @param forceRefresh 강제 새로 고침 여부
     * @return List<CommunityResponseDto> 커뮤니티 글 목록
     */
    override suspend fun getCommunityPostList(forceRefresh: Boolean): List<CommunityResponseDto> {
        // 현재 시간 가져오기
        val currentTime = System.currentTimeMillis()
        return if (forceRefresh || cachedCommunityPostList.isNullOrEmpty() || currentTime - lastFetchTimeCommunity > CACHE_VALIDITY_DURATION_COMMUNITY) {
            // 강제 새로 고침이 필요하거나 캐시가 비어있거나 캐시 유효 기간이 지난 경우
            // PostApi를 사용하여 전체 커뮤니티 글 목록 요청
            val response = postApi.getAllCommunityPost()
            // 응답 결과를 캐시에 저장
            cachedCommunityPostList = response.result
            lastFetchTimeCommunity = currentTime
            // 캐시된 커뮤니티 글 목록 반환
            cachedCommunityPostList!!
        } else {
            // 유효한 캐시가 있는 경우 캐시된 목록 반환
            cachedCommunityPostList!!
        }
    }


    /**
     *  뉴스 목록 요청 (GET)
     *  @param forceRefresh 강제 새로 고침 여부
     *  @return List<NewsList> 뉴스 목록
     */
    override suspend fun getNewsList(forceRefresh: Boolean) : List<NewsList> {
        val currentTime = System.currentTimeMillis()
        return if (cachedNewsList.isNullOrEmpty() || currentTime - lastNewsFetchTime > CACHE_VALIDITY_DURATION_NEWS) {
            // PostApi를 사용하여 뉴스 목록 요청
            val response = postApi.getNewsList()
            // 응답 결과를 캐시에 저장
            cachedNewsList = response.result
            lastNewsFetchTime = currentTime
            // 캐시된 뉴스 목록 반환
            cachedNewsList!!
        } else {
            // 유효한 캐시가 있는 경우 캐시된 목록 반환
            cachedNewsList!!
        }
    }


    /**
     * 커뮤니티 글 상세 요청 (GET)
     * @param postId 요청할 글의 ID
     * @return CommunityDetailResponseDto 글 상세 정보
     * @throws Exception 요청 중 발생한 예외
     */
    override suspend fun getCommunityPostDetail(postId: Long): CommunityDetailResponseDto {
        return try {
            // PostApi를 사용하여 글 상세 요청
            val response = postApi.getCommunityPostDetail(postId)
            response.result
        } catch (e: Exception) {
            throw e
        }
    }


    /**
     * 캐시 무효화
     * 캐시된 게시글 목록을 초기화
     * 캐시된 분석글 목록을 초기화
     * 마지막으로 게시글 목록을 가져온 시간을 0으로 초기화
     * 마지막으로 분석글 목록을 가져온 시간을 0으로 초기화
     */
    private fun clearCache() {
        cachedCommunityPostList = null
        cachedNewsList = null
        lastFetchTimeCommunity = 0
        lastNewsFetchTime = 0
    }

    class AuthException(message: String) : Exception(message)  // 로그인 관련 예외 클래스
    class ApiException(message: String) : Exception(message)   // API 요청 관련 예외 클래스
}
