package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.WatchlistCoinPrice
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

class GetCoinWatchlistUseCase @Inject constructor(
    private val coinRepository: CoinRepository
){
    suspend operator fun invoke() : Result<List<WatchlistCoinPrice>> {
        return try {
            val coinWatchlist = coinRepository.getCoinWatchlist()
            Result.success(coinWatchlist)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}