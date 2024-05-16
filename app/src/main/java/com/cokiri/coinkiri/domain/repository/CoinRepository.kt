package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.domain.model.Coin

interface CoinRepository {
    suspend fun getCoins(): List<Coin>
}