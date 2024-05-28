package com.cokiri.coinkiri.data.repository

import android.util.Log
import com.cokiri.coinkiri.data.remote.api.CoinApi
import com.cokiri.coinkiri.data.remote.mapper.CoinMapper
import com.cokiri.coinkiri.data.remote.model.CoinPrice
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val coinApi: CoinApi
) : CoinRepository {

    // 로그를 위한 태그
    companion object { private const val TAG = "CoinRepositoryImpl" }

    // 캐시된 코인 리스트
    private var cachedCoins: List<Coin>? = null

    override suspend fun getCoins(): List<Coin> {
        return try {
            // 캐시된 코인 리스트가 없으면 API를 통해 코인 리스트를 가져옴
            if (cachedCoins.isNullOrEmpty()) {
                val coinResponses = coinApi.getCoins()
                val coins = CoinMapper.mapToCoins(coinResponses)
                cachedCoins = coins
                coins
            } else {
                // 캐시된 코인 리스트가 있으면 캐시된 코인 리스트를 반환
                cachedCoins!!
            }
        } catch (e: Exception) {
            // 오류 발생 시 빈 리스트 반환
            Log.e(TAG, "코인을 가져오는 중에 오류가 발생했습니다. : ${e.message}")
            emptyList()
        }
    }


    override suspend fun getCoinDaysInfo(coinId: String): List<CoinPrice> {
        return try {
            val coinDaysResponse = coinApi.getCoinDaysInfo(coinId.toLong())
            coinDaysResponse.result.coinPrices
        } catch (e: Exception) {
            Log.e(TAG, "코인 정보를 가져오는 중에 오류가 발생했습니다. : ${e.message}")
            emptyList()
        }
    }


}