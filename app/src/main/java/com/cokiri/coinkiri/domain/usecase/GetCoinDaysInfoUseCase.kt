package com.cokiri.coinkiri.domain.usecase

import android.util.Log
import com.cokiri.coinkiri.data.remote.model.CoinPrice
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

class GetCoinDaysInfoUseCase @Inject constructor(
    private val coinRepository: CoinRepository
){
    suspend operator fun invoke(coinId: String): Result<List<CoinPrice>> {
        return try {
            val coinDaysInfo = coinRepository.getCoinDaysInfo(coinId)
            Result.success(coinDaysInfo)
        } catch (e: Exception) {
            Log.e("GetCoinDaysInfoUseCase", "Failed to get coin days info", e)
            Result.failure(e)
        }
    }
}