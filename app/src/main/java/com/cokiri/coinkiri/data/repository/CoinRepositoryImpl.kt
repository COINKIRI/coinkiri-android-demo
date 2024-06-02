package com.cokiri.coinkiri.data.repository

import android.util.Log
import com.cokiri.coinkiri.data.remote.PreferencesManager
import com.cokiri.coinkiri.data.remote.api.CoinApi
import com.cokiri.coinkiri.data.remote.mapper.CoinMapper
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CoinPrice
import com.cokiri.coinkiri.data.remote.model.WatchlistCoinPrice
import com.cokiri.coinkiri.data.remote.model.WatchlistResponse
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val coinApi: CoinApi,
    private val preferencesManager: PreferencesManager
) : CoinRepository {

    // 로그를 위한 태그
    companion object {
        private const val TAG = "CoinRepositoryImpl"
    }

    // 캐시된 코인 리스트
    private var cachedCoins: List<Coin>? = null

    /**
     * 코인 목록을 가져옴
     */
    override suspend fun getCoins(): List<Coin> {
        return try {
            // 캐시된 코인 리스트가 없으면 API를 통해 코인 리스트를 가져옴
            if (cachedCoins.isNullOrEmpty()) {
                val coinResponses = coinApi.getCoins()
                val coins = CoinMapper.mapToCoins(coinResponses)
                cachedCoins = coins
                coins
            } else {
                // 캐시된 코인 리스트가 있으면 캐시된 코인 리스트를 반환
                cachedCoins!!
            }
        } catch (e: Exception) {
            // 오류 발생 시 빈 리스트 반환
            Log.e(TAG, "코인을 가져오는 중에 오류가 발생했습니다. : ${e.message}")
            emptyList()
        }
    }


    /**
     * 특정 코인의 일간(200일) 가격 정보를 가져옴
     */
    override suspend fun getCoinDaysInfo(coinId: String): List<CoinPrice> {
        return try {
            val coinDaysResponse = coinApi.getCoinDaysInfo(coinId.toLong())
            coinDaysResponse.result.coinPrices
        } catch (e: Exception) {
            Log.e(TAG, "코인 정보를 가져오는 중에 오류가 발생했습니다. : ${e.message}")
            emptyList()
        }
    }


    /**
     * 코인 관심 목록에 추가
     */
    override suspend fun addCoinToWatchlist(coinId: Long): ApiResponse {
        // 액세스 토큰을 preferencesManager에서 가져옴
        val accessToken = preferencesManager.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            throw AuthException("로그인이 필요합니다.")
        }

        // PostApi를 사용하여 글 작성 요청
        val response = coinApi.addCoinToWatchlist("Bearer $accessToken", coinId)
        Log.d(TAG, "submitPost: $response")

        if (response.isSuccessful) {
            // 응답 데이터 반환, null인 경우 예외 발생
            return response.body() ?: throw ApiException("응답 데이터가 null입니다.")
        } else {
            throw ApiException("응답이 실패하였습니다.")
        }
    }


    /**
     * 코인 관심 목록에서 삭제
     */
    override suspend fun deleteCoinFromWatchlist(coinId: Long): ApiResponse {
        // 액세스 토큰을 preferencesManager에서 가져옴
        val accessToken = preferencesManager.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            throw AuthException("로그인이 필요합니다.")
        }

        // PostApi를 사용하여 글 작성 요청
        val response = coinApi.deleteCoinFromWatchlist("Bearer $accessToken", coinId)
        Log.d(TAG, "submitPost: $response")

        if (response.isSuccessful) {
            // 응답 데이터 반환, null인 경우 예외 발생
            return response.body() ?: throw ApiException("응답 데이터가 null입니다.")
        } else {
            throw ApiException("응답이 실패하였습니다.")
        }
    }


    /**
     * 코인 관심 목록 등록여부 조회
     */
    override suspend fun checkCoinInWatchlist(coinId: Long): Boolean {
        val accessToken = preferencesManager.getAccessToken()
        if (accessToken.isNullOrEmpty()) {
            throw AuthException("로그인이 필요합니다.")
        }

        val response = coinApi.checkCoinInterest("Bearer $accessToken", coinId)
        Log.d(TAG, "checkCoinInWatchlist: $response")

        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null && apiResponse.result is Boolean) {
                return apiResponse.result as Boolean
            } else {
                throw ApiException("Invalid response format")
            }
        } else {
            throw ApiException("응답이 실패하였습니다.")
        }
    }


    /**
     * 코인 관심 목록 조회
     */
    override suspend fun getCoinWatchlist(): List<WatchlistCoinPrice> {
        return try{
            val accessToken = preferencesManager.getAccessToken()
            if (accessToken.isNullOrEmpty()) {
                throw AuthException("로그인이 필요합니다.")
            }

            val response = coinApi.getCoinWatchlist("Bearer $accessToken")
            Log.d(TAG, "getCoinWatchlist: $response")
            response.result.coinPrices
        } catch (e: Exception) {
            Log.e(TAG, "코인 관심 목록을 가져오는 중에 오류가 발생했습니다. : ${e.message}")
            emptyList()
        }
    }


    class AuthException(message: String) : Exception(message)
    class ApiException(message: String) : Exception(message)
}