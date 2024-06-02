package com.cokiri.coinkiri.presentation.analysis

import android.util.Log
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.domain.usecase.GetCoinsUseCase
import com.cokiri.coinkiri.domain.usecase.WebSocketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val webSocketUseCase: WebSocketUseCase
) : ViewModel() {

    // 코인 목록
    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList.asStateFlow()

    // 선택한 코인의 티커
    private val _singleCoinTicker = MutableStateFlow<Ticker?>(null)
    val singleCoinTicker: StateFlow<Ticker?> = _singleCoinTicker.asStateFlow()

    // 선택한 코인의 아이디
    private val _selectedCoinId = MutableStateFlow(0L)
    val selectedCoinId: StateFlow<Long> = _selectedCoinId.asStateFlow()

    // 선택한 코인의 이름
    private val _selectedCoinName = MutableStateFlow("")
    val selectedCoinName: StateFlow<String> = _selectedCoinName.asStateFlow()

    // 선택한 코인의 마켓 아이디
    private val _selectedCoinMarketId = MutableStateFlow("")
    val selectedCoinMarketId: StateFlow<String> = _selectedCoinMarketId.asStateFlow()

    // 선택한 코인의 심볼 이미지
    private val _selectedCoinImagePainter = MutableStateFlow<Painter?>(null)
    val selectedCoinImagePainter: StateFlow<Painter?> = _selectedCoinImagePainter.asStateFlow()

    // 선택한 코인의 전일 종가
    private val _selectedCoinPrevClosingPrice = MutableStateFlow("")
    val selectedCoinPrevClosingPrice: StateFlow<String> = _selectedCoinPrevClosingPrice.asStateFlow()

    // 목표기간 설정(1개월, 3개월, 6개월, 1년)
    private val _selectedTargetPeriod = MutableStateFlow("")
    val selectedTargetPeriod: StateFlow<String> = _selectedTargetPeriod.asStateFlow()

    // 목표기간 설정(직접입력)
    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()

    // 투자 의견 선택(강력매수, 매수, 중립, 매도, 강력매도)
    private val _selectedInvestmentOption = MutableStateFlow("")
    val selectedInvestmentOption: StateFlow<String> = _selectedInvestmentOption.asStateFlow()

    // 목표가격 설정
    private val _selectedTargetPrice = MutableStateFlow("")
    val selectedTargetPrice: StateFlow<String> = _selectedTargetPrice.asStateFlow()

    // 목표가격의 변동률
    private val _selectedTargetPriceChangeRate = MutableStateFlow("")
    val selectedTargetPriceChangeRate: StateFlow<String> = _selectedTargetPriceChangeRate.asStateFlow()


    /**
     * 코인 목록을 가져옴
     */
    fun fetchCoins() {
        viewModelScope.launch {
            val coins = getCoinsUseCase()
            _coinList.value = coins
        }
    }

    /**
     * 코인 마켓명으로 웹소켓을 통해 해당 코인의 티커를 가져옴
     */
    fun observeCoinTicker(coinMarketId: String) {
        viewModelScope.launch {
            webSocketUseCase.startConnection(listOf(coinMarketId), { ticker ->
                _singleCoinTicker.value = ticker
            }, receiveOnce = true)
        }
    }

    /**
     * 선택된 코인의 정보(아이디, 마켓아이디, 이름, 심볼이미지)를 저장
     */
    fun setSelectedCoin(coinId: Long, coinMarketId: String, coinName: String, coinSymbolImage: Painter) {
        _selectedCoinId.value = coinId
        _selectedCoinMarketId.value = coinMarketId
        _selectedCoinName.value = coinName
        _selectedCoinImagePainter.value = coinSymbolImage
        Log.d("AnalysisViewModel", "coinId: $coinId, coinMarketId: $coinMarketId, coinName: $coinName")
    }

    /**
     * 선택된 투자의견을 저장
     */
    fun setSelectedInvestmentOption(option: String) {
        _selectedInvestmentOption.value = option
        Log.d("AnalysisViewModel", "투자의견: $option")
    }



    /**
     * 선택된 코인의 전일 종가를 저장
     */
    fun setPrevClosingPrice(price: String) {
        _selectedCoinPrevClosingPrice.value = price
        Log.d("AnalysisViewModel", "전일종가: $price")
    }

    /**
     * 선택한 목표기간을 저장
     */
    fun setTargetPeriod(targetPeriod: String) {
        _selectedTargetPeriod.value = targetPeriod
        Log.d("AnalysisViewModel", "목표기간: $targetPeriod")
    }

    /**
     * 선택한 날짜를 저장
     */
    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
        Log.d("AnalysisViewModel", "달력 선택기간: $date")
    }

    /**
     * 선택한 목표가격을 저장
     */
    fun setSelectedTargetPrice(targetPrice: String) {
        _selectedTargetPrice.value = targetPrice
        Log.d("AnalysisViewModel", "목표가격: $targetPrice")
    }

    /**
     * 목표가격의 변동률을 저장
     */
    fun setTargetPriceChangeRate(rate: String) {
        _selectedTargetPriceChangeRate.value = rate
        Log.d("AnalysisViewModel", "목표가격 변동률: $rate")
    }

    /**
     * 상태 초기화
     */
    fun resetState() {
        _selectedCoinId.value = 0L
        _selectedCoinMarketId.value = ""
        _selectedCoinName.value = ""
        _selectedCoinImagePainter.value = null
        _selectedCoinPrevClosingPrice.value = ""
        _selectedInvestmentOption.value = ""
        _selectedTargetPrice.value = ""
        _selectedTargetPeriod.value = ""
        _selectedDate.value = null
    }
}
