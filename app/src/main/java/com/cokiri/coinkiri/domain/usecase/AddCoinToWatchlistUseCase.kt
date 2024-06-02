package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

class AddCoinToWatchlistUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(coinId: Long): Result<ApiResponse> {
        return runCatching { coinRepository.addCoinToWatchlist(coinId) }
    }
}