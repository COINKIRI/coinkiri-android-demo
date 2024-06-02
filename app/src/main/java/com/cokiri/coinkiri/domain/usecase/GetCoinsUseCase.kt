package com.cokiri.coinkiri.domain.usecase

import android.util.Log
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(): Result<List<Coin>> {
        return try {
            val coins = coinRepository.getCoins()
            Result.success(coins)
        } catch (e: Exception) {
            Log.e("GetCoinsUseCase", "Failed to get coins", e)
            Result.failure(e)
        }
    }
}