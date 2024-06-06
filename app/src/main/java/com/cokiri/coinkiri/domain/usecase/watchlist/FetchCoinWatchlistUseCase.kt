package com.cokiri.coinkiri.domain.usecase.watchlist

import com.cokiri.coinkiri.data.remote.model.WatchlistCoinPrice
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

/**
 * 코인 관심목록을 가져오는 UseCase
 */
class FetchCoinWatchlistUseCase @Inject constructor(
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