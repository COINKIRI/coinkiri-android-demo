package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.CoinPrice
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

class GetCoinDaysInfoUseCase @Inject constructor(
    private val coinRepository: CoinRepository
){
    suspend operator fun invoke(coinId: String): List<CoinPrice> {
        return coinRepository.getCoinDaysInfo(coinId)
    }
}