package com.cokiri.coinkiri.domain.usecase.watchlist

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

/**
 * 코인 관심목록 삭제 UseCase
 */
class DeleteCoinFromWatchlistUseCase @Inject constructor(
    private val coinRepository: CoinRepository
){
    suspend operator fun invoke(coinId: Long): Result<ApiResponse> {
        return runCatching { coinRepository.deleteCoinFromWatchlist(coinId) }
    }
}