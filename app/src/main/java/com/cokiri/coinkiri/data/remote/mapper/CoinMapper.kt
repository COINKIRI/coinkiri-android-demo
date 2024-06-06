package com.cokiri.coinkiri.data.remote.mapper

import com.cokiri.coinkiri.data.remote.model.coin.CoinInfo
import com.cokiri.coinkiri.data.remote.model.coin.CoinListResponse
import com.cokiri.coinkiri.domain.model.Coin

object CoinMapper {

    // CoinInfo Coin으로 변환하는 함수
    private fun mapToCoin(coinInfo: CoinInfo): Coin {
        return Coin(
            coinId = coinInfo.id,
            market = coinInfo.market,
            koreanName = coinInfo.koreanName,
            englishName = coinInfo.englishName,
            symbolImage = coinInfo.symbolImage
        )
    }

    // CoinResponse() List<Coin>으로 변환하는 함수
    fun mapToCoins(coinResponses: CoinListResponse): List<Coin> {
        return coinResponses.result.map { coinInfo ->
            mapToCoin(coinInfo)
        }
    }
}