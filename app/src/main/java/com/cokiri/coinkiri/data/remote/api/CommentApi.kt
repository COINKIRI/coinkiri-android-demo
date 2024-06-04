package com.cokiri.coinkiri.data.remote.api

import com.cokiri.coinkiri.data.remote.AuthRequired
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommentRequest
import com.cokiri.coinkiri.data.remote.model.CommentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApi {


    /**
     * 댓글 조회 API
     */
    @GET("/api/v1/comment/{postId}")
    suspend fun getComment(@Path("postId") postId: Long) : CommentResponse


    /**
     * 댓글 작성 API
     */
    @AuthRequired
    @Headers("Content-Type: application/json")
    @POST("/api/v1/comment/save")
    suspend fun submitComment(
        @Header("Authorization") accessToken: String,
        @Body commentRequest: CommentRequest
    ): Response<ApiResponse>
}