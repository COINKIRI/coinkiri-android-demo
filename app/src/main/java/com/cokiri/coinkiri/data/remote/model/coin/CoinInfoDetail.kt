package com.cokiri.coinkiri.data.remote.model.coin

import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.model.Ticker

data class CoinInfoDetail(
    val coin: Coin,
    val ticker: Ticker?
)