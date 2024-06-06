package com.cokiri.coinkiri.presentation.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity
import com.cokiri.coinkiri.data.remote.model.coin.WatchlistCoinPrice
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.domain.repository.UserRepository
import com.cokiri.coinkiri.domain.usecase.WebSocketUseCase
import com.cokiri.coinkiri.domain.usecase.watchlist.FetchCoinWatchlistUseCase
import com.cokiri.coinkiri.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val webSocketUseCase: WebSocketUseCase,
    private val fetchCoinWatchlistUseCase: FetchCoinWatchlistUseCase
) : BaseViewModel() {

    private val _memberInfo = MutableStateFlow<MemberInfoEntity?>(null)
    val memberInfo: StateFlow<MemberInfoEntity?> = _memberInfo

    private val _coinWatchlist = MutableStateFlow<List<WatchlistCoinPrice>>(emptyList())
    val coinWatchlist: StateFlow<List<WatchlistCoinPrice>> = _coinWatchlist

    // 티커 정보를 저장하는 StateFlow
    private val _tickers = MutableStateFlow<Map<String, Ticker>>(emptyMap())
    val tickers: StateFlow<Map<String, Ticker>> = _tickers.asStateFlow()


    init {
        loadMemberInfo()
    }

    private fun loadMemberInfo() {
        viewModelScope.launch {
            _memberInfo.value = userRepository.getMemberInfo()
        }
    }

    fun fetchCoinWatchlist() {
        executeWithLoading(
            block = { fetchCoinWatchlistUseCase() },
            onSuccess = { coinWatchlist ->
                _coinWatchlist.value = coinWatchlist
            }
        )
    }


    /**
     * 특정 코인들의 티커 정보를 가져오는 함수
     */
    fun getTickers(coinMarketIds: List<String>) {
        Log.d("ProfileViewModel", "getTickers: $coinMarketIds")
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
        }
    }

}