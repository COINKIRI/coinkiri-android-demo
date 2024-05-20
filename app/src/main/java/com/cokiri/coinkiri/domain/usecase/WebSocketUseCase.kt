package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.domain.repository.WebSocketRepository
import com.cokiri.coinkiri.presentation.price.PriceViewModel
import com.cokiri.coinkiri.presentation.price.UpbitWebSocketCallback
import javax.inject.Inject

/**
 * 웹소켓 시작,종료를 담당하는 UseCase
 */

class WebSocketUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository
) {
    fun startConnection(krwMarkets: String) {
        webSocketRepository.startWebSocketConnection(krwMarkets)
    }

    fun closeConnection() {
        webSocketRepository.closeWebSocketConnection()
    }
}