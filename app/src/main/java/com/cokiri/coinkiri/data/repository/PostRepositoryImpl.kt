package com.cokiri.coinkiri.data.repository

import com.cokiri.coinkiri.data.remote.PreferencesManager
import com.cokiri.coinkiri.data.remote.api.PostApi
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.PostDataRequest
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postApi: PostApi,
    private val preferencesManager: PreferencesManager
) : PostRepository {

    override suspend fun submitPost(postDataRequest: PostDataRequest) : Result<ApiResponse> {
        return try {
            val accessToken = preferencesManager.getAccessToken()
            if (accessToken.isNullOrEmpty()) {
                return Result.failure(Exception("로그인이 필요합니다."))
            } else {
                // 로그인이 필요한 API 요청에는 토큰을 헤더에 추가하여 전송합니다.
                val response = postApi.submitPost("Bearer $accessToken", postDataRequest)

                if (response.isSuccessful) {
                    // 응답이 성공적으로 수신되었을 때만 응답 데이터를 추출하여 반환합니다.
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
}