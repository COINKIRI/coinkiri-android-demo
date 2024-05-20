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
                    webSocketUseCase.startConnection(krwMarket)
                }
            }
        }
    }

    override fun onUpbitTickerResponseReceived(ticker: Ticker) {
        val updatedResponses = _tickers.value.toMutableMap()
        updatedResponses[ticker.code] = ticker
        _tickers.value = updatedResponses
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