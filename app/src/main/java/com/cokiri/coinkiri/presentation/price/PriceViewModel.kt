package com.cokiri.coinkiri.presentation.price

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.CoinInfoDetail
import com.cokiri.coinkiri.data.remote.model.CoinPrice
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.domain.usecase.GetCoinDaysInfoUseCase
import com.cokiri.coinkiri.domain.usecase.GetCoinsUseCase
import com.cokiri.coinkiri.domain.usecase.WebSocketUseCase
import com.cokiri.coinkiri.extensions.executeWithLoading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,                // 코인 정보를 가져오는 유스케이스
    private val webSocketUseCase: WebSocketUseCase,              // WebSocket을 관리하는 유스케이스
    private val getCoinDaysInfoUseCase: GetCoinDaysInfoUseCase   // 특정 코인의 일간 정보를 가져오는 유스케이스
) : ViewModel() {

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

    // 로딩 상태를 저장하는 StateFlow
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 에러 메시지를 저장하는 StateFlow
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // ViewModel 초기화 시 코인 목록을 로드
    init {
        loadCoins()
    }


    /**
     * 코인 목록을 로드하는 함수 (코인 정보 가져오기)
     */
    private fun loadCoins() {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                getCoinsUseCase() // 코인 정보를 가져오는 유스케이스 호출
            }?.let { coins ->
                _coinList.value = coins // 코인 목록 갱신
                _krwMarketString.value = convertCoinInfoResponse(coins) // KRW 시장 문자열 갱신
            }
        }
    }


    /**
     *  특정 코인의 일간 가격 정보를 가져오는 함수
     */
    fun getCoinDaysInfo(coinId: String) {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                getCoinDaysInfoUseCase(coinId)     // 특정 코인의 일간 정보 가져오기
            }?.let { coinDaysInfo ->
                _coinDaysInfo.value = coinDaysInfo // 일간 정보 갱신
            }
        }
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
        }
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
