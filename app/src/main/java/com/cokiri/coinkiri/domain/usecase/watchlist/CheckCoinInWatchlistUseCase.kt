package com.cokiri.coinkiri.domain.usecase.watchlist

import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

/**
 * 코인이 관심 목록에 있는지 확인하는 UseCase
 */
class CheckCoinInWatchlistUseCase @Inject constructor(
    private val coinRepository: CoinRepository
){
    suspend operator fun invoke(coinId: Long): Boolean {
        return runCatching { coinRepository.checkCoinInWatchlist(coinId) }
            .getOrElse {
                // 예외 발생 시 false 반환
                false
            }
    }
}