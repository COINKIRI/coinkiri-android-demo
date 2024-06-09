package com.cokiri.coinkiri.domain.usecase.coin

import com.cokiri.coinkiri.data.remote.model.coin.CoinTalk
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

/**
 * 코인톡 조회 UseCase
 */
class FetchCoinTalkUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(coinId: Long): Result<List<CoinTalk>> {
        return runCatching { coinRepository.fetchCoinTalkList(coinId) }
    }
}