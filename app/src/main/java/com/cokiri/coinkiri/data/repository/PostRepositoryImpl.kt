package com.cokiri.coinkiri.data.repository

import android.util.Log
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
}