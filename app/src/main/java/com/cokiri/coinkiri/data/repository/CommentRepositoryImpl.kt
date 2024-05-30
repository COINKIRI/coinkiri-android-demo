package com.cokiri.coinkiri.data.repository

import android.util.Log
import com.cokiri.coinkiri.data.remote.PreferencesManager
import com.cokiri.coinkiri.data.remote.api.PostApi
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommentList
import com.cokiri.coinkiri.data.remote.model.CommentRequest
import com.cokiri.coinkiri.domain.repository.CommentRepository
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val postApi: PostApi,
    private val preferencesManager: PreferencesManager
) : CommentRepository {

    companion object {
        private const val TAG = "CommentRepositoryImpl"
    }

    // 댓글 목록 요청(GET)
    override suspend fun getCommentList(postId: Long): List<CommentList> {
        return try {
            val response = postApi.getComment(postId)
            Log.d(TAG, "getCommentList: $response")
            response.result

        } catch (e: Exception) {
            Log.d(TAG, "getCommentList: $e")
            emptyList()
        }
    }


    // 댓글 작성 요청(POST)
    override suspend fun submitComment(commentRequest: CommentRequest): Result<ApiResponse> {
        return try {
            val accessToken = preferencesManager.getAccessToken()
            if (accessToken.isNullOrEmpty()) {
                return Result.failure(Exception("로그인이 필요합니다."))
            } else {
                val response = postApi.submitComment("Bearer $accessToken", commentRequest)
                Log.d(TAG, "submitComment: $response")

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
            Log.d(TAG, "submitComment: $e")
            Result.failure(e)
        }
    }
}