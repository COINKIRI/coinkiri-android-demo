package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.domain.model.Ticker

/**
 * WebSocket을 통해 데이터를 받아오기 위한 WebSocketRepository Interface
 */
interface WebSocketRepository {
    fun startWebSocketConnection(krwMarkets: List<String>, onTickerReceived: (Ticker) -> Unit)
    fun closeWebSocketConnection()
}