package com.cokiri.coinkiri.domain.usecase

import android.util.Log
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.domain.service.WebSocketService
import javax.inject.Inject

/**
 * 웹소켓 시작,종료를 담당하는 UseCase
 */

class WebSocketUseCase @Inject constructor(
    private val webSocketService: WebSocketService
) {
    fun startConnection(markets: List<String>, onMessage: (Ticker) -> Unit, receiveOnce: Boolean = false) {
        try {
            webSocketService.startConnection(markets, onMessage, receiveOnce)
        } catch (e: Exception) {
            Log.e("WebSocketUseCase", "Failed to start WebSocket connection", e)
        }
    }

    fun closeConnection() {
        try {
            webSocketService.closeConnection()
        } catch (e: Exception) {
            Log.e("WebSocketUseCase", "Failed to close WebSocket connection", e)
        }
    }
}