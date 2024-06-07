package com.cokiri.coinkiri.presentation.price

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.coin.CoinInfoDetail
import com.cokiri.coinkiri.data.remote.model.coin.CoinPrice
import com.cokiri.coinkiri.data.remote.model.coin.WatchlistCoinPrice
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.domain.usecase.WebSocketUseCase
import com.cokiri.coinkiri.domain.usecase.coin.GetCoinDaysInfoUseCase
import com.cokiri.coinkiri.domain.usecase.coin.GetCoinsUseCase
import com.cokiri.coinkiri.domain.usecase.watchlist.AddCoinToWatchlistUseCase
import com.cokiri.coinkiri.domain.usecase.watchlist.CheckCoinInWatchlistUseCase
import com.cokiri.coinkiri.domain.usecase.watchlist.DeleteCoinFromWatchlistUseCase
import com.cokiri.coinkiri.domain.usecase.watchlist.FetchCoinWatchlistUseCase
import com.cokiri.coinkiri.util.logLongMessage
import com.cokiri.coinkiri.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,                                 // 코인 정보를 가져오는 유스케이스
    private val webSocketUseCase: WebSocketUseCase,                               // WebSocket을 관리하는 유스케이스
    private val getCoinDaysInfoUseCase: GetCoinDaysInfoUseCase,                   // 특정 코인의 일간 정보를 가져오는 유스케이스
    private val addCoinToWatchlistUseCase: AddCoinToWatchlistUseCase,             // 코인 관심 목록에 추가하는 유스케이스
    private val fetchCoinWatchlistUseCase: FetchCoinWatchlistUseCase,             // 코인 관심 목록을 가져오는 유스케이스
    private val checkCoinInWatchlistUseCase: CheckCoinInWatchlistUseCase,         // 코인 관심 목록 등록여부 조회 유스케이스
    private val deleteCoinFromWatchlistUseCase: DeleteCoinFromWatchlistUseCase    // 코인 관심 목록에서 삭제하는 유스케이스
) : BaseViewModel() {

    // 코인 목록을 저장하는 StateFlow
    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList.asStateFlow()

    // KRW 시장 문자열을 저장하는 StateFlow
    private val _krwMarketString = MutableStateFlow("")
    private val krwMarketString: StateFlow<String> = _krwMarketString.asStateFlow()

    // 티커 정보를 저장하는 StateFlow
    private val _tickers = MutableStateFlow<Map<String, Ticker>>(emptyMap())
    val tickers: StateFlow<Map<String, Ticker>> = _tickers.asStateFlow()

    // 코인 상세 정보를 저장하는 StateFlow
    private val _coinInfoDetailList = MutableStateFlow<List<CoinInfoDetail>>(emptyList())
    val coinInfoDetailList: StateFlow<List<CoinInfoDetail>> = _coinInfoDetailList.asStateFlow()

    // 단일 코인 티커 정보를 저장하는 StateFlow
    private val _singleCoinTicker = MutableStateFlow<Ticker?>(null)
    val singleCoinTicker: StateFlow<Ticker?> = _singleCoinTicker.asStateFlow()

    // 특정 코인의 일간 가격 정보를 저장하는 StateFlow
    private val _coinDaysInfo = MutableStateFlow<List<CoinPrice>>(emptyList())
    val coinDaysInfo: StateFlow<List<CoinPrice>> get() = _coinDaysInfo


    // 관심 목록에 코인이 있는지 여부를 저장하는 StateFlow
    private val _isCoinInWatchlist = MutableStateFlow(false)
    val isCoinInWatchlist: StateFlow<Boolean> get() = _isCoinInWatchlist


    private val _coinWatchlist = MutableStateFlow<List<WatchlistCoinPrice>>(emptyList())
    val coinWatchlist: StateFlow<List<WatchlistCoinPrice>> = _coinWatchlist


    // 상승률 상위 5개 코인을 저장하는 StateFlow
    private val _topGainers = MutableStateFlow<List<CoinInfoDetail>>(emptyList())
    val topGainers: StateFlow<List<CoinInfoDetail>> = _topGainers

    // 하락률 상위 5개 코인을 저장하는 StateFlow
    private val _topLosers = MutableStateFlow<List<CoinInfoDetail>>(emptyList())
    val topLosers: StateFlow<List<CoinInfoDetail>> = _topLosers.asStateFlow()


    // ViewModel 초기화 시 코인 목록을 로드
    init {
        loadCoins()
    }


    /**
     * 코인 목록을 로드하는 함수 (코인 정보 가져오기)
     */
    private fun loadCoins() {
        executeWithLoading(
            block = { getCoinsUseCase() },
            onSuccess = { coins ->
                _coinList.value = coins
                _krwMarketString.value = convertCoinInfoResponse(coins)
            }
        )
    }


    /**
     *  특정 코인의 일간 가격 정보를 가져오는 함수
     */
    fun getCoinDaysInfo(coinId: String) {
        executeWithLoading(
            block = { getCoinDaysInfoUseCase(coinId) },
            onSuccess = { coinDaysInfo ->
                _coinDaysInfo.value = coinDaysInfo
            }
        )
    }


    /**
     * KRW 시장 문자열을 관찰하는 함수
     */
    fun observeKrwMarketString() {
        viewModelScope.launch {
            krwMarketString.collect { krwMarket ->
                if (krwMarket.isNotBlank()) {
                    // WebSocket 연결 시작
                    webSocketUseCase.startConnection(listOf(krwMarket), { ticker ->
                        handleTickerResponse(ticker) // 티커 응답 처리
                    }, receiveOnce = false)
                }
            }
        }
    }


    /**
     * 단일 코인 티커 정보를 관찰하는 함수
     */
    fun observeSingleCoinTicker(coinMarketId: String) {
        viewModelScope.launch {
            // WebSocket 연결 시작
            webSocketUseCase.startConnection(listOf(coinMarketId), { ticker ->
                _singleCoinTicker.value = ticker // 단일 코인 티커 갱신
            }, receiveOnce = false)
        }
    }

    /**
     * 특정 코인들의 티커 정보를 가져오는 함수
     */
    fun getTickers(coinMarketIds: List<String>) {
        viewModelScope.launch {
            // WebSocket 연결 시작
            webSocketUseCase.startConnection(coinMarketIds, { ticker ->
                handleTickerResponse(ticker) // 티커 응답 처리
            }, receiveOnce = false)
        }
    }


    /**
     * 티커 응답을 처리하는 함수
     */
    private fun handleTickerResponse(ticker: Ticker) {
        viewModelScope.launch {
            val updatedResponses = _tickers.value.toMutableMap()
            updatedResponses[ticker.code] = ticker // 티커 정보 갱신
            _tickers.value = updatedResponses
            updateCoinInfoDetails(ticker) // 코인 상세 정보 갱신
        }
    }


    /**
     * 코인 상세 정보를 갱신하는 함수
     */
    private fun updateCoinInfoDetails(ticker: Ticker) {
        val coinInfoDetails = _coinInfoDetailList.value.toMutableList()
        val coin = _coinList.value.find { it.krwMarket == ticker.code }
        if (coin != null) {
            val index = coinInfoDetails.indexOfFirst { it.coin == coin }
            val updatedCoinInfoDetail = CoinInfoDetail(coin, ticker)
            if (index != -1) {
                coinInfoDetails[index] = updatedCoinInfoDetail // 기존 코인 정보 업데이트
            } else {
                coinInfoDetails.add(updatedCoinInfoDetail) // 새로운 코인 정보 추가
            }
            _coinInfoDetailList.value = coinInfoDetails // 코인 상세 정보 리스트 갱신
            updateTopCoins() // 상승률과 하락률이 높은 상위 5개의 코인 갱신
        }
    }


    /**
     * 코인 관심 목록에 추가하는 함수
     */
    fun addCoinToWatchlist(coinId: Long) {
        executeWithLoading(
            block = { addCoinToWatchlistUseCase(coinId) },
            onSuccess = { _isCoinInWatchlist.value = true }
        )
    }


    /**
     * 코인 관심 목록에서 삭제하는 함수
     */
    fun deleteCoinFromWatchlist(coinId: Long) {
        executeWithLoading(
            block = { deleteCoinFromWatchlistUseCase(coinId) },
            onSuccess = { _isCoinInWatchlist.value = false }
        )
    }


    /**
     * 코인 관심 목록 등록여부 조회하는 함수
     */
    fun checkIfCoinInWatchlist(coinId: Long) {
        viewModelScope.launch {
            try {
                val inWatchlist = checkCoinInWatchlistUseCase(coinId)
                _isCoinInWatchlist.value = inWatchlist
            } catch (e: Exception) {
                Log.e("PriceViewModel", "Error checking watchlist status", e)
            }
        }
    }

    /**
     * 코인 관심 목록을 가져오는 함수
     */
    fun fetchCoinWatchlist() {
        executeWithLoading(
            block = { fetchCoinWatchlistUseCase() },
            onSuccess = { coinWatchlist ->
                _coinWatchlist.value = coinWatchlist
            }
        )
    }


    /**
     * 상승률과 하락률이 높은 상위 5개의 코인을 갱신하는 함수
     */
    private fun updateTopCoins() {
        viewModelScope.launch {
            val topCoins = getTopCoins()
            _topGainers.value = topCoins.first
            _topLosers.value = topCoins.second
            logLongMessage("Top gainers updated", _topGainers.value.toString())
            logLongMessage("Top losers updated", _topLosers.value.toString())
        }
    }


    /**
     * 상승률과 하락률이 높은 상위 5개의 코인을 반환하는 함수
     */
    private fun getTopCoins(): Pair<List<CoinInfoDetail>, List<CoinInfoDetail>> {
        val sortedCoinsByGain = _coinInfoDetailList.value.sortedByDescending { it.ticker?.signedChangeRate }
        val topGainers = sortedCoinsByGain.take(5)

        val sortedCoinsByLoss = _coinInfoDetailList.value.sortedBy { it.ticker?.signedChangeRate }
        val topLosers = sortedCoinsByLoss.take(5)

        return Pair(topGainers, topLosers)
    }


    /**
     * 코인 목록을 KRW 시장 문자열로 변환하는 함수
     */
    private fun convertCoinInfoResponse(coins: List<Coin>): String {
        return coins.joinToString(", ") { it.krwMarket }
    }


    /**
     * WebSocket 연결을 종료하는 함수
     */
    fun stopWebSocketConnection() {
        webSocketUseCase.closeConnection()
    }


    /**
     * ViewModel 소멸 시 WebSocket 연결 종료
     */
    override fun onCleared() {
        super.onCleared()
        webSocketUseCase.closeConnection()
    }
}
