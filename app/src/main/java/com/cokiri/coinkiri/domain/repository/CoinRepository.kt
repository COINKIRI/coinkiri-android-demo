package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.remote.model.CoinPrice
import com.cokiri.coinkiri.domain.model.Coin

interface CoinRepository {
    suspend fun getCoins(): List<Coin>

    suspend fun getCoinDaysInfo(coinId: String) : List<CoinPrice>

}