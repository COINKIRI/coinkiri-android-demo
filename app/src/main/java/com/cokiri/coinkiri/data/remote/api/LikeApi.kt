package com.cokiri.coinkiri.data.remote.api

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.service.auth.AuthRequired
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeApi {

    /**
     * 좋아요 추가 API
     */
    @AuthRequired
    @Headers("Content-Type: application/json")
    @POST("/api/v1/like/{postId}")
    suspend fun addLike(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Long
    ): Response<ApiResponse>


    /**
     * 좋아요 취소 API
     */
    @AuthRequired
    @Headers("Content-Type: application/json")
    @DELETE("/api/v1/like/delete/{postId}")
    suspend fun deleteLike(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Long
    ): Response<ApiResponse>


    /**
     * 좋아요 여부 확인 API
     */
    @AuthRequired
    @Headers("Content-Type: application/json")
    @GET("/api/v1/like/check/{postId}")
    suspend fun checkLike(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Long
    ): Response<ApiResponse>

}