package com.cokiri.coinkiri.domain.repository

/**
 * WebSocket을 통해 데이터를 받아오기 위한 WebSocketRepository Interface
 */
interface WebSocketRepository {
    fun startWebSocketConnection(krwMarkets: String)
    fun closeWebSocketConnection()
}