package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(): List<Coin> {
        return coinRepository.getCoins()
    }
}