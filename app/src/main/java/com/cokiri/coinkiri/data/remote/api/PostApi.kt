package com.cokiri.coinkiri.data.remote.api

import com.cokiri.coinkiri.data.remote.AuthRequired
import com.cokiri.coinkiri.data.remote.model.AnalysisPostDataRequest
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommentRequest
import com.cokiri.coinkiri.data.remote.model.CommentResponse
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponse
import com.cokiri.coinkiri.data.remote.model.CommunityResponse
import com.cokiri.coinkiri.data.remote.model.PostRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApi {

    // 커뮤니티 게시글 작성
    @AuthRequired
    @Headers("Content-Type: application/json")
    @POST("/api/v1/post/community/save")
    suspend fun submitPost(
        @Header("Authorization") accessToken: String,
        @Body postRequestDto: PostRequestDto
    ): Response<ApiResponse>


    // 커뮤니티 게시글 전체조회
    @GET("/api/v1/post/community/all")
    suspend fun getAllCommunityPost(): CommunityResponse


    // 커뮤니티 게시글 상세조회
    @GET("/api/v1/post/community/{postId}")
    suspend fun getCommunityPostDetail(@Path("postId") postId : Long) : CommunityDetailResponse


    // 게시글 댓글 조회
    @GET("/api/v1/comment/{postId}")
    suspend fun getComment(@Path("postId") postId: Long) : CommentResponse

    // 게시글 댓글 작성
    @AuthRequired
    @Headers("Content-Type: application/json")
    @POST("/api/v1/comment/save")
    suspend fun submitComment(
        @Header("Authorization") accessToken: String,
        @Body commentRequest: CommentRequest
    ): Response<ApiResponse>



    // 분석글 작성
    @AuthRequired
    @Headers("Content-Type: application/json")
    @POST("/api/v1/analysis/save")
    suspend fun submitAnalysisPost(
        @Header("Authorization") accessToken: String,
        @Body analysisPostDataRequest: AnalysisPostDataRequest
    ): Response<ApiResponse>

}