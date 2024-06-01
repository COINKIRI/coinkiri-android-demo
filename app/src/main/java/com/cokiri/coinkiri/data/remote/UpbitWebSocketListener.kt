package com.cokiri.coinkiri.data.remote

import android.util.Log
import com.cokiri.coinkiri.data.remote.mapper.TickerMapper
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.util.JsonParser
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.nio.charset.StandardCharsets
import java.util.UUID

/**
 * UpbitWebSocketListener
 * 웹소켓 리스너
 */
class UpbitWebSocketListener(
    private val onTickerReceived: (Ticker) -> Unit,
    private val krwMarkets: List<String>,
    private val jsonParser: JsonParser,
    private val normalClosureStatus: Int,
    private val receiveOnce: Boolean
) : WebSocketListener() {

    // 웹소켓이 연결되었을 때 호출
    override fun onOpen(webSocket: WebSocket, response: Response) {
        val uniqueTicket = UUID.randomUUID().toString()
        val marketsJsonArray = krwMarkets.joinToString(",") { "\"$it\"" }
        val replaceMarketsJsonArray = marketsJsonArray.replace("\"", "")
        val json = """[{"ticket":"$uniqueTicket"},{"type":"ticker","codes":[$replaceMarketsJsonArray]}]"""
        webSocket.send(json)
    }

    // 웹소켓으로부터 텍스트 메시지를 받았을 때 호출
    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocket", "Received text: $text")
    }

    // 웹소켓으로부터 바이트 메시지를 받았을 때 호출
    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        val jsonString = bytes.string(StandardCharsets.UTF_8)
        val upbitTickerResponse = jsonParser.fromJsonToUpbitTickerResponse(jsonString)   // JsonParser를 통해 UpbitTickerResponse로 변환
        if (upbitTickerResponse != null) {
            val ticker = TickerMapper.UpbitTickerResponseToTicker(upbitTickerResponse)   // UpbitTickerResponse를 Ticker로 변환
            onTickerReceived(ticker)
            if (receiveOnce) {
                webSocket.close(normalClosureStatus, null)
            }
        } else {
            Log.d("WebSocket", "Failed to parse response")
        }
    }

    // 웹소켓이 종료되기 직전에 호출
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(normalClosureStatus, null)
        webSocket.cancel()
    }

    // 웹소켓이 종료되었을 때 호출
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocket", "Socket Closed: $code / $reason")
    }

    // 웹소켓이 에러가 발생했을 때 호출
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("WebSocket", "Socket Error: ${t.message}")
    }
}
