package com.cokiri.coinkiri.presentation.price

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.CoinInfoDetail
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.domain.usecase.GetCoinsUseCase
import com.cokiri.coinkiri.domain.usecase.WebSocketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val webSocketUseCase: WebSocketUseCase
) : ViewModel(), UpbitWebSocketCallback {

    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList

    private val _krwMarketString = MutableStateFlow("")
    private val krwMarketString: StateFlow<String> = _krwMarketString.asStateFlow()

    private val _tickers = MutableStateFlow<Map<String, Ticker>>(emptyMap())
    private val tickers: StateFlow<Map<String, Ticker>> = _tickers.asStateFlow()

    private val _coinInfoDetailList = MutableStateFlow<List<CoinInfoDetail>>(emptyList())
    val coinInfoDetailList: StateFlow<List<CoinInfoDetail>> = _coinInfoDetailList.asStateFlow()


    init {
        loadCoins()
        observeKrwMarketString()
    }

    private fun loadCoins() {
        viewModelScope.launch {
            val coins = getCoinsUseCase()
            _coinList.value = coins
            _krwMarketString.value = convertCoinInfoResponse(coins)
        }
    }

    private fun observeKrwMarketString() {
        viewModelScope.launch {
            krwMarketString.collect { krwMarket ->
                if (krwMarket.isNotBlank()) {
                    webSocketUseCase.startConnection(krwMarket) { ticker ->
                        handleTickerResponse(
                            ticker
                        )
                    }
                }
            }
        }
    }

    // 인터페이스 콜백
    override fun onUpbitTickerResponseReceived(ticker: Ticker) {
        viewModelScope.launch {
            val updatedResponses = _tickers.value.toMutableMap()
            updatedResponses[ticker.code] = ticker
            _tickers.value = updatedResponses
            updateCoinInfoDetails(ticker)
        }
    }

    // 람다 콜백
    private fun handleTickerResponse(ticker: Ticker) {
        viewModelScope.launch {
            Log.d("PriceViewModel", "Received ticker: $ticker")
            val updatedResponses = _tickers.value.toMutableMap()
            updatedResponses[ticker.code] = ticker
            _tickers.value = updatedResponses
            updateCoinInfoDetails(ticker)
        }
    }


    // coin + ticker 정보를 coinInfoDetailList에 업데이트
    private fun updateCoinInfoDetails(ticker: Ticker) {
        val coinInfoDetails = _coinInfoDetailList.value.toMutableList()
        val coin = _coinList.value.find { it.krwMarket == ticker.code }
        if (coin != null) {
            val index = coinInfoDetails.indexOfFirst { it.coin == coin }
            if (index != -1) {
                coinInfoDetails[index] = CoinInfoDetail(coin, ticker)
            } else {
                coinInfoDetails.add(CoinInfoDetail(coin, ticker))
            }
            _coinInfoDetailList.value = coinInfoDetails
        }
    }



    override fun onCleared() {
        super.onCleared()
        webSocketUseCase.closeConnection()
    }

    private fun convertCoinInfoResponse(coins: List<Coin>): String {
        return coins.joinToString(", ") { it.krwMarket }
    }


    fun byteArrayToPainter(string: String?): BitmapPainter {
        val byteArraySymbolImage = Base64.getDecoder().decode(string)
        val bitmap =
            BitmapFactory.decodeByteArray(byteArraySymbolImage, 0, byteArraySymbolImage.size)
        return BitmapPainter(bitmap.asImageBitmap())
    }
}