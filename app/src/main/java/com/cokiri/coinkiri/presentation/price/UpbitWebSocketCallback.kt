package com.cokiri.coinkiri.presentation.price

import com.cokiri.coinkiri.domain.model.Ticker

/**
 * WebSocket으로부터 받은 데이터를 처리하기 위한 콜백
 * viewModel에서 WebSocket으로부터 받은 데이터를 처리하기 위해 사용
 */

interface UpbitWebSocketCallback {

    // WebSocket으로부터 받은 데이터를 처리하기 위한 메서드
    fun onUpbitTickerResponseReceived(ticker: Ticker)
}