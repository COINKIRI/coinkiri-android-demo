package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.coin.CoinPrice
import com.cokiri.coinkiri.data.remote.model.coin.CoinTalk
import com.cokiri.coinkiri.data.remote.model.coin.CoinTalkRequest
import com.cokiri.coinkiri.data.remote.model.coin.WatchlistCoinPrice
import com.cokiri.coinkiri.domain.model.Coin

interface CoinRepository {

    // 코인 목록을 가져옴
    suspend fun coinList(): List<Coin>

    // 특정 코인의 일간(200일) 가격 정보를 가져옴
    suspend fun getCoinDaysInfo(coinId: String) : List<CoinPrice>

    // 코인 관심 목록에 추가
    suspend fun addCoinToWatchlist(coinId: Long) : ApiResponse

    // 코인 관심 목록에서 삭제
    suspend fun deleteCoinFromWatchlist(coinId: Long) : ApiResponse

    // 코인 관심 목록 등록여부 조회
    suspend fun checkCoinInWatchlist(coinId: Long) : Boolean

    // 코인 관심 목록을 가져옴
    suspend fun getCoinWatchlist() : List<WatchlistCoinPrice>

    // 코인톡 작성
    suspend fun submitCoinTalk(coinTalkRequest: CoinTalkRequest) : ApiResponse

    // 코인톡 조회
    suspend fun fetchCoinTalkList(coinId: Long) : List<CoinTalk>

}