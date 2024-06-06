package com.cokiri.coinkiri.data.remote.api

import com.cokiri.coinkiri.data.remote.service.auth.AuthRequired
import com.cokiri.coinkiri.data.remote.model.auth.MemberResponse
import com.cokiri.coinkiri.data.remote.model.auth.SignUpRequest
import com.cokiri.coinkiri.data.remote.model.auth.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Auth API
 * - 회원가입, 로그아웃
 */
interface AuthApi {

    /**
     * 회원가입 API
     */
    @AuthRequired
    @Headers("Content-Type: application/json")
    @POST("/api/v1/auth/signup")
    suspend fun signUpUser(@Body request: SignUpRequest) : SignUpResponse


    /**
     * 로그아웃 API
     */
    @AuthRequired
    @Headers("Content-Type: application/json")
    @POST("/api/v1/auth/logout")
    suspend fun logoutUser(@Header("Authorization") accessToken: String)


    /**
     * 사용자 정보 조회 API
     */
    @AuthRequired
    @Headers("Content-Type: application/json")
    @GET("/api/v1/member/info")
    suspend fun getMemberInfo(@Header("Authorization") accessToken: String) : MemberResponse

}