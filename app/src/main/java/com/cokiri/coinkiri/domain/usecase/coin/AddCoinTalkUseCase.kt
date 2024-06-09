package com.cokiri.coinkiri.domain.usecase.coin

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.coin.CoinTalkRequest
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

class AddCoinTalkUseCase @Inject constructor(
    private val coinRepository: CoinRepository
){
    suspend operator fun invoke(coinTalkRequest: CoinTalkRequest): Result<ApiResponse> {
        return runCatching { coinRepository.submitCoinTalk(coinTalkRequest) }
    }
}