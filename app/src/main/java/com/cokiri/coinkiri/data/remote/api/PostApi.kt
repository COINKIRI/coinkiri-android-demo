package com.cokiri.coinkiri.data.remote.api

import com.cokiri.coinkiri.data.remote.AuthRequired
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponse
import com.cokiri.coinkiri.data.remote.model.CommunityResponse
import com.cokiri.coinkiri.data.remote.model.PostDataRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApi {

    @AuthRequired
    @Headers("Content-Type: application/json")
    @POST("/api/v1/post/community/save")
    suspend fun submitPost(
        @Header("Authorization") accessToken: String,
        @Body postDataRequest: PostDataRequest
    ): Response<ApiResponse>


    @GET("/api/v1/post/community/all")
    suspend fun getAllCommunityPost(): CommunityResponse

    @GET("/api/v1/post/community/{postId}")
    suspend fun getCommunityPostDetail(@Path("postId") postId : Long) : CommunityDetailResponse
}