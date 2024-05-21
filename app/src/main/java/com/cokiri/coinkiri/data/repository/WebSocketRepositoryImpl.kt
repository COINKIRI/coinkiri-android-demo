package com.cokiri.coinkiri.data.repository

import com.cokiri.coinkiri.data.remote.UpbitWebSocketListener
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.domain.repository.WebSocketRepository
import com.cokiri.coinkiri.presentation.price.UpbitWebSocketCallback
import com.cokiri.coinkiri.util.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject
import javax.inject.Singleton

/**
 * WebSocketRepositoryImpl
 * WebSocketRepository의 구현체로 WebSocket을 통해 데이터를 받아오는 역할
 */

@Singleton
class WebSocketRepositoryImpl @Inject constructor(
    private val client: OkHttpClient,
    private val callback: UpbitWebSocketCallback,
    private val jsonParser: JsonParser
) : WebSocketRepository {

    private val NORMAL_CLOSURE_STATUS = 1000
    private lateinit var webSocket: WebSocket

    override fun startWebSocketConnection(krwMarkets: String, onTickerReceived: (Ticker) -> Unit){
        val request = Request.Builder()
            .url("https://api.upbit.com/websocket/v1")
            .build()

        webSocket = client.newWebSocket(request, UpbitWebSocketListener(callback, onTickerReceived ,krwMarkets, jsonParser, NORMAL_CLOSURE_STATUS))
    }

    override fun closeWebSocketConnection() {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }
}