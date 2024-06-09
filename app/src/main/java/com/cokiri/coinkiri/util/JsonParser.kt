package com.cokiri.coinkiri.util

import com.cokiri.coinkiri.data.remote.model.coin.UpbitTickerResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Json 파싱을 담당하는 클래스
 * upbit의 ticker 웹소켓 응답을 파싱하기 위해 사용
 */

@Singleton
class JsonParser @Inject constructor(
    private val moshi: Moshi
) {
    // JsonAdapter를 통해 UpbitTickerResponse로 변환
    private val adapter: JsonAdapter<UpbitTickerResponse> = moshi.adapter(UpbitTickerResponse::class.java)

    // Json을 UpbitTickerResponse로 변환
    fun fromJsonToUpbitTickerResponse(json: String): UpbitTickerResponse? {
        return adapter.fromJson(json)
    }
}