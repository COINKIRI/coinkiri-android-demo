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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val webSocketUseCase: WebSocketUseCase,
    private val getCoinDaysInfoUseCase: GetCoinDaysInfoUseCase
) : ViewModel() {

    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList.asStateFlow()

    private val _krwMarketString = MutableStateFlow("")
    private val krwMarketString: StateFlow<String> = _krwMarketString.asStateFlow()

    private val _tickers = MutableStateFlow<Map<String, Ticker>>(emptyMap())
    val tickers: StateFlow<Map<String, Ticker>> = _tickers.asStateFlow()

    private val _coinInfoDetailList = MutableStateFlow<List<CoinInfoDetail>>(emptyList())
    val coinInfoDetailList: StateFlow<List<CoinInfoDetail>> = _coinInfoDetailList.asStateFlow()

    private val _singleCoinTicker = MutableStateFlow<Ticker?>(null)
    val singleCoinTicker: StateFlow<Ticker?> = _singleCoinTicker.asStateFlow()

    private val _coinDaysInfo = MutableStateFlow<List<CoinPrice>>(emptyList())
    val coinDaysInfo: StateFlow<List<CoinPrice>> get() = _coinDaysInfo


    init {
        loadCoins()
    }

    private fun loadCoins() {
        viewModelScope.launch {
            val coins = getCoinsUseCase()
            _coinList.value = coins
            _krwMarketString.value = convertCoinInfoResponse(coins)
        }
    }


    fun getCoinDaysInfo(coinId: String) {
        viewModelScope.launch {
            val coinDaysInfo = getCoinDaysInfoUseCase(coinId)
            _coinDaysInfo.value = coinDaysInfo
        }
    }


    fun observeKrwMarketString() {
        viewModelScope.launch {
            krwMarketString.collect { krwMarket ->
                if (krwMarket.isNotBlank()) {
                    webSocketUseCase.startConnection(listOf(krwMarket), { ticker ->
                        handleTickerResponse(ticker)
                    }, receiveOnce = false)  // receiveOnce를 true로 설정
                }
            }
        }
    }

    fun observeSingleCoinTicker(coinMarketId: String) {
        viewModelScope.launch {
            webSocketUseCase.startConnection(listOf(coinMarketId), { ticker ->
                _singleCoinTicker.value = ticker
            }, receiveOnce = false)  // receiveOnce를 true로 설정
        }
    }


    // 람다 콜백
    private fun handleTickerResponse(ticker: Ticker) {
        viewModelScope.launch {
            val updatedResponses = _tickers.value.toMutableMap()
            updatedResponses[ticker.code] = ticker
            _tickers.value = updatedResponses
            updateCoinInfoDetails(ticker)
        }
    }




    // 코인 정보 리스트 업데이트
    private fun updateCoinInfoDetails(ticker: Ticker) {
        val coinInfoDetails = _coinInfoDetailList.value.toMutableList()
        val coin = _coinList.value.find { it.krwMarket == ticker.code }
        if (coin != null) {
            val index = coinInfoDetails.indexOfFirst { it.coin == coin }
            val updatedCoinInfoDetail = CoinInfoDetail(coin, ticker)
            if (index != -1) {
                coinInfoDetails[index] = updatedCoinInfoDetail
            } else {
                coinInfoDetails.add(updatedCoinInfoDetail)
            }
            _coinInfoDetailList.value = coinInfoDetails
        }
    }

    private fun convertCoinInfoResponse(coins: List<Coin>): String {
        return coins.joinToString(", ") { it.krwMarket }
    }


    // WebSocket 연결 종료 메서드
    fun stopWebSocketConnection() {
        webSocketUseCase.closeConnection()
    }

    override fun onCleared() {
        super.onCleared()
        webSocketUseCase.closeConnection()
    }
}