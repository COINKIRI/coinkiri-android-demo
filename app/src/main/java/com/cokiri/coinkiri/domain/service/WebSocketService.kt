package com.cokiri.coinkiri.domain.service

import com.cokiri.coinkiri.domain.model.Ticker

/**
 * WebSocketService
 * WebSocket을 통해 데이터를 받아오는 역할
 */
interface WebSocketService {
    fun startConnection(krwMarkets: List<String>, onTickerReceived: (Ticker) -> Unit, receiveOnce: Boolean = false)
    fun closeConnection()
}
