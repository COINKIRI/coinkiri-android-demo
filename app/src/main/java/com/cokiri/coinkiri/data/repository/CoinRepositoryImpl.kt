package com.cokiri.coinkiri.data.repository

import android.util.Log
import com.cokiri.coinkiri.data.remote.api.CoinApi
import com.cokiri.coinkiri.data.remote.mapper.CoinMapper
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val coinApi: CoinApi
) : CoinRepository {

    companion object { private const val TAG = "CoinRepositoryImpl" }

    override suspend fun getCoins(): List<Coin> {
        return try {
            val coinResponses = coinApi.getCoins()
            CoinMapper.mapToCoins(coinResponses)

        } catch (e: Exception) {
            // 오류가 발생하면 오류 메시지를 로그에 기록하고 빈 리스트를 반환
            Log.e(TAG, "코인을 가져오는 중에 오류가 발생했습니다. : ${e.message}")
            emptyList()
        }
    }
}