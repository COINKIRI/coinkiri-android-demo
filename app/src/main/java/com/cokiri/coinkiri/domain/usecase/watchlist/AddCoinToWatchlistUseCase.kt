package com.cokiri.coinkiri.domain.usecase.watchlist

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

/**
 * 코인을 관심코인에 추가하는 UseCase
 */
class AddCoinToWatchlistUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(coinId: Long): Result<ApiResponse> {
        return runCatching { coinRepository.addCoinToWatchlist(coinId) }
    }
}