package com.cokiri.coinkiri.data.remote.api

import com.cokiri.coinkiri.data.remote.model.CoinDaysResponse
import com.cokiri.coinkiri.data.remote.model.CoinResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinApi {

    // 전체 코인목록 조회
    @GET("/api/v1/coin/all")
    suspend fun getCoins(): CoinResponse

    @GET("/api/v1/coin/{coinId}")
    suspend fun getCoinDaysInfo(@Path("coinId") coinId: Long): CoinDaysResponse
}