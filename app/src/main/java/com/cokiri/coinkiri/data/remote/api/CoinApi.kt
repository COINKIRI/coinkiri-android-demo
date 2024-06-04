package com.cokiri.coinkiri.data.remote.api

import com.cokiri.coinkiri.data.remote.AuthRequired
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CoinDaysResponse
import com.cokiri.coinkiri.data.remote.model.CoinResponse
import com.cokiri.coinkiri.data.remote.model.WatchlistResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface CoinApi {

    /**
     * 코인 목록 조회
     */
    @GET("/api/v1/coin/all")
    suspend fun getCoins(): CoinResponse


    /**
     * 특정 코인의 일간 가격 정보 조회(200일)
     * @param coinId 코인 ID
     */
    @GET("/api/v1/coin/{coinId}")
    suspend fun getCoinDaysInfo(@Path("coinId") coinId: Long): CoinDaysResponse


    /**
     * 코인 관심 목록 등록
     */
    @AuthRequired
    @Headers("Content-Type: application/json")
    @POST("/api/v1/interest/{coinId}")
    suspend fun addCoinToWatchlist(
        @Header("Authorization") accessToken: String,
        @Path("coinId") coinId: Long
    ): Response<ApiResponse>


    /**
     * 코인 관심 목록 삭제
     */
    @AuthRequired
    @Headers("Content-Type: application/json")
    @DELETE("/api/v1/interest/delete/{coinId}")
    suspend fun deleteCoinFromWatchlist(
        @Header("Authorization") accessToken: String,
        @Path("coinId") coinId: Long
    ): Response<ApiResponse>


    /**
     * 코인 관심 목록 등록여부 조회
     */
    @AuthRequired
    @GET("/api/v1/interest/check/{coinId}")
    suspend fun checkCoinInterest(
        @Header("Authorization") accessToken: String,
        @Path("coinId") coinId: Long
    ): Response<ApiResponse>


    /**
     * 코인 관심 목록 조회
     */
    @AuthRequired
    @GET("/api/v1/interest/")
    suspend fun getCoinWatchlist(
        @Header("Authorization") accessToken: String
    ): WatchlistResponse
}