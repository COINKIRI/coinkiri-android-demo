package com.cokiri.coinkiri.data.remote.api

import com.cokiri.coinkiri.data.remote.service.auth.AuthRequired
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.post.community.CommunityDetailResponse
import com.cokiri.coinkiri.data.remote.model.post.community.CommunityResponse
import com.cokiri.coinkiri.data.remote.model.post.news.NewsListResponse
import com.cokiri.coinkiri.data.remote.model.analysis.PostRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApi {

    /**
     * 커뮤니티 게시글 작성 API
     */
    @AuthRequired
    @Headers("Content-Type: application/json")
    @POST("/api/v1/post/community/save")
    suspend fun submitPost(
        @Header("Authorization") accessToken: String,
        @Body postRequestDto: PostRequestDto
    ): Response<ApiResponse>


    /**
     * 커뮤니티 게시글 조회 API
     */
    @GET("/api/v1/post/community/all")
    suspend fun getAllCommunityPost(): CommunityResponse


    /**
     * 커뮤니티 게시글 상세 조회 API
     */
    @GET("/api/v1/post/community/{postId}")
    suspend fun getCommunityPostDetail(@Path("postId") postId : Long) : CommunityDetailResponse


    /**
     * 뉴스 조회 API
     */
    @GET("/api/v1/news/list")
    suspend fun getNewsList(): NewsListResponse

}