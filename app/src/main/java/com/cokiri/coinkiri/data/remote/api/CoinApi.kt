package com.cokiri.coinkiri.data.remote.api

import com.cokiri.coinkiri.data.remote.model.CoinResponse
import retrofit2.http.GET

interface CoinApi {

    @GET("/api/v1/coin/all")
    suspend fun getCoins(): CoinResponse

}