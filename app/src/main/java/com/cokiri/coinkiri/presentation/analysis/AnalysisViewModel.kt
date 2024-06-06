package com.cokiri.coinkiri.presentation.analysis

import android.util.Log
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.AnalysisDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.AnalysisResponseDto
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.domain.usecase.analysis.FetchAllAnalysisPostsUseCase
import com.cokiri.coinkiri.domain.usecase.analysis.FetchAnalysisDetailUseCase
import com.cokiri.coinkiri.domain.usecase.coin.GetCoinsUseCase
import com.cokiri.coinkiri.domain.usecase.WebSocketUseCase
import com.cokiri.coinkiri.extensions.executeWithLoading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val webSocketUseCase: WebSocketUseCase,
    private val fetchAllAnalysisPostsUseCase: FetchAllAnalysisPostsUseCase,
    private val fetchAnalysisDetailUseCase: FetchAnalysisDetailUseCase
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

    // 분석글 목록을 관리하는 MutableStateFlow
    private val _analysisPostList = MutableStateFlow<List<AnalysisResponseDto>>(emptyList())
    val analysisPostList: StateFlow<List<AnalysisResponseDto>> = _analysisPostList.asStateFlow()

    // 선택한 분석글의 상세 정보를 관리하는 MutableStateFlow
    private val _analysisDetail = MutableStateFlow<AnalysisDetailResponseDto?>(null)
    val analysisDetail: StateFlow<AnalysisDetailResponseDto?> = _analysisDetail

    // 코인 티커들을 관리하는 Map
    private val _coinTickers = MutableStateFlow<Map<String, Ticker?>>(emptyMap())
    val coinTickers: StateFlow<Map<String, Ticker?>> = _coinTickers.asStateFlow()

    // 코인 로딩 상태를 관리하는 Map
    private val _loadingStates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val loadingStates: StateFlow<Map<String, Boolean>> = _loadingStates.asStateFlow()



    // 로딩 상태와 에러 메시지 관리용 MutableStateFlow
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    /**
     * 초기화 블록
     */
    init {
        // 초기 데이터 로드(분석글 목록)
        fetchAllAnalysisPostList()
    }


    /**
     * 분석글 목록을 가져오는 함수
     */
    fun fetchAllAnalysisPostList() {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                val result = fetchAllAnalysisPostsUseCase()
                if (result.isSuccess) {
                    _analysisPostList.value = result.getOrDefault(emptyList())
                }
                result
            }
        }
    }


    /**
     * 분석글 상세 정보를 가져오는 함수
     * @param postId 가져올 게시글의 ID
     */
    suspend fun fetchAnalysisDetail(postId: Long) {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                val result = fetchAnalysisDetailUseCase(postId)
                if (result.isSuccess) {
                    _analysisDetail.value = result.getOrNull()
                }
                result
            }
        }
    }


    /**
     * 코인 목록을 가져옴
     */
    fun fetchCoins() {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                getCoinsUseCase().onSuccess { coins ->
                    _coinList.value = coins
                    Result.success(Unit)
                }.onFailure { e ->
                    Result.failure<Unit>(e)
                }
            }
        }
    }


    /**
     * 코인 마켓명으로 웹소켓을 통해 해당 코인의 티커를 가져옴(한번만 받음)
     */
    fun observeCoinTicker(coinMarketId: String) {
        viewModelScope.launch {
            webSocketUseCase.startConnection(listOf(coinMarketId), { ticker ->
                _singleCoinTicker.value = ticker
            }, receiveOnce = true)
        }
    }


    // 코인 마켓명으로 웹소켓을 통해 해당 코인의 티커를 가져옴(계속 받음)
    fun observeCoinTickerContinuously(coinMarketId: String) {
        viewModelScope.launch {
            _loadingStates.update { it + (coinMarketId to true) }
            webSocketUseCase.startConnection(listOf(coinMarketId), { ticker ->
                _coinTickers.update { it + (coinMarketId to ticker) }
                _loadingStates.update { it + (coinMarketId to false) }
            }, receiveOnce = false)
        }
    }

    // 특정 코인의 티커를 반환하는 메서드
    fun getCoinTicker(coinMarketId: String): StateFlow<Ticker?> {
        return _coinTickers.map { it[coinMarketId] }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    }

    // 특정 코인의 로딩 상태를 반환하는 메서드
    fun isLoading(coinMarketId: String): StateFlow<Boolean> {
        return _loadingStates.map { it[coinMarketId] ?: false }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
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


    /**
     * 웹 소켓 연결 종료
     */
    fun closeWebSocketConnection() {
        webSocketUseCase.closeConnection()
    }


}
