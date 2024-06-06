package com.cokiri.coinkiri.data.remote.service.websocket

import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.domain.service.WebSocketService
import com.cokiri.coinkiri.util.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject
import javax.inject.Singleton

/**
 * WebSocketServiceImpl
 * WebSocket을 통해 데이터를 받아오는 역할
 */
@Singleton
class WebSocketServiceImpl @Inject constructor(
    private val client: OkHttpClient,
    private val jsonParser: JsonParser
) : WebSocketService {

    private val NORMAL_CLOSURE_STATUS = 1000
    private lateinit var webSocket: WebSocket

    override fun startConnection(krwMarkets: List<String>, onTickerReceived: (Ticker) -> Unit, receiveOnce: Boolean) {
        val request = Request.Builder()
            .url("https://api.upbit.com/websocket/v1")
            .build()

        webSocket = client.newWebSocket(request, UpbitWebSocketListener(onTickerReceived, krwMarkets, jsonParser, NORMAL_CLOSURE_STATUS, receiveOnce))
    }

    override fun closeConnection() {
        if (this::webSocket.isInitialized) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
        }
    }
}
