package com.cokiri.coinkiri.domain.usecase.coin

import android.util.Log
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(): Result<List<Coin>> {
        return try {
            val coinList = coinRepository.coinList()
            Result.success(coinList)
        } catch (e: Exception) {
            Log.e("GetCoinsUseCase", "Failed to get coins", e)
            Result.failure(e)
        }
    }
}