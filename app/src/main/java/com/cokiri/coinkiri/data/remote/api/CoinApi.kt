package com.cokiri.coinkiri.data.remote.api

import com.cokiri.coinkiri.data.remote.model.CoinResponse
import retrofit2.http.GET

interface CoinApi {

    @GET("/coin/all/v1")
    suspend fun getCoins(): CoinResponse

}