package com.cokiri.coinkiri.data.remote.api

import com.cokiri.coinkiri.data.remote.model.AnalysisDetailResponse
import com.cokiri.coinkiri.data.remote.model.AnalysisPostDataRequest
import com.cokiri.coinkiri.data.remote.model.AnalysisResponse
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.service.auth.AuthRequired
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AnalysisApi {

    /**
     * 분석 게시글 작성 API
     */
    @AuthRequired
    @Headers("Content-Type: application/json")
    @POST("/api/v1/analysis/save")
    suspend fun submitAnalysisPost(
        @Header("Authorization") accessToken: String,
        @Body analysisPostDataRequest: AnalysisPostDataRequest
    ): Response<ApiResponse>


    /**
     * 분석 전체 게시글 조회 API
     */
    @GET("/api/v1/analysis/all")
    suspend fun getAllAnalysisPost(): AnalysisResponse


    /**
     * 분석글 게시글 상세 조회 API
     */
    @GET("/api/v1/analysis/{postId}")
    suspend fun getAnalysisPostDetail(@Path("postId") postId : Long) : AnalysisDetailResponse
}