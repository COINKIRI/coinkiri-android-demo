package com.cokiri.coinkiri.data.repository

import android.util.Log
import com.cokiri.coinkiri.data.remote.PreferencesManager
import com.cokiri.coinkiri.data.remote.api.PostApi
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponse
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.CommunityResponseDto
import com.cokiri.coinkiri.data.remote.model.PostDataRequest
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postApi: PostApi,
    private val preferencesManager: PreferencesManager
) : PostRepository {

    private var cachedCommunityPostList: List<CommunityResponseDto>? = null


    // 글작성 요청(POST)
    override suspend fun submitPost(postDataRequest: PostDataRequest): Result<ApiResponse> {
        return try {
            val accessToken = preferencesManager.getAccessToken()
            if (accessToken.isNullOrEmpty()) {
                return Result.failure(Exception("로그인이 필요합니다."))
            } else {

                val response = postApi.submitPost("Bearer $accessToken", postDataRequest)
                Log.d("PostRepositoryImpl", "submitPost: $response")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Result.success(responseBody)
                    } else {
                        Result.failure(Exception("응답 데이터가 null입니다."))
                    }
                } else {
                    Result.failure(Exception("응답이 실패하였습니다."))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // 전체 커뮤니티 글 목록 요청(GET)
    override suspend fun getCommunityPostList(): List<CommunityResponseDto> {
        return if (cachedCommunityPostList.isNullOrEmpty()) {
            val response = postApi.getAllCommunityPost()
            cachedCommunityPostList = response.result // response에서 result 리스트 추출
            cachedCommunityPostList!!
        } else {
            cachedCommunityPostList!!
        }
    }

    // 필요에 따라 캐시를 지우는 함수 추가
    fun clearCache() {
        cachedCommunityPostList = null
    }

    // 커뮤니티 글 상세 요청(GET)
    override suspend fun getCommunityPostDetail(postId: Long): CommunityDetailResponseDto {
        return try {
            val response = postApi.getCommunityPostDetail(postId)
            response.result
        } catch (e: Exception) {
            throw e
        }
    }

}