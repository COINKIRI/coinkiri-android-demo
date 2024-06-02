package com.cokiri.coinkiri.domain.model

import java.text.DecimalFormat

data class Ticker(
    val type : String,                      // ticker
    val code : String,                      // KRW-BTC
    val openingPrice : Double?,             // 시가
    val highPrice : Double?,                // 고가
    val lowPrice : Double?,                 // 저가
    val tradePrice : Double?,               // 현재가
    val prevClosingPrice : Double?,         // 전일 종가
    val accTradePrice : Double?,            // 누적 거래대금(UTC 0시 기준)
    val change : String?,                   // 전일 대비
    val changePrice : Double?,              // 부호 없는 전일 대비 가격
    val signedChangePrice : Double?,        // 전일 대비 가격
    val changeRate : Double?,               // 전일 대비 가격 변동률
    val signedChangeRate : Double?,         // 부호 있는 전일 대비 가격 변동률
    val askBid : String?,                   // 매수/매도
    val tradeVolume : Double,               // 최근 24시간 거래량
    val accTradeVolume : Double?,           // 누적 거래량
    val tradeDate : String?,                // 최근 거래 일자(UTC)
    val tradeTime : String?,                // 최근 거래 시각(UTC)
    val tradeTimestamp : Long?,             // 최근 거래 타임스탬프
    val accAskVolume : Double?,             // 누적 매도량
    val accBidVolume : Double?,             // 누적 매수량
    val highest52WeekPrice : Double?,       // 52주 신고가
    val highest52WeekDate : String?,        // 52주 신고가 달성일
    val lowest52WeekPrice : Double?,        // 52주 신저가
    val lowest52WeekDate : String?,         // 52주 신저가 달성일
    val marketState : String?,              // 마켓 상태
    val isTradingSuspended : Boolean?,      // 거래 정지 여부
    val delistingDate : Any?,               // 상장 폐지일
    val marketWarning : String?,            // 유의 종목 여부
    val timestamp : Long?,                  // 타임스탬프
    val accTradePrice24h : Double?,         // 누적 거래대금(UTC 0시 기준)
    val accTradeVolume24h : Double?,        // 누적 거래량(UTC 0시 기준)
    val streamType : String?                // 스트림 타입
) {

    val formattedSignedChangeRate : String
        get() = String.format("%.2f", signedChangeRate?.times(100) ?: 0.0) + "%"

    private val valueInMillions = accTradePrice24h?.div(1_000_000)
    private val formattedValue: String = DecimalFormat("#,###").format(valueInMillions)
    val formattedAccTradePrice24h : String
        get() = "${formattedValue}백만"

    val formattedTradePrice : String
        get() = tradePrice?.let { DecimalFormat("#,###").format(it) } ?: "0"

    val formattedHighPrice : String
        get() = highPrice?.let { DecimalFormat("#,###").format(it) } ?: "0"

    val formattedLowPrice : String
        get() = lowPrice?.let { DecimalFormat("#,###").format(it) } ?: "0"

    val formattedPrevClosingPrice : String
        get() = prevClosingPrice?.let { DecimalFormat("#,###").format(it) } ?: "0"

    val formattedSignedChangePrice : String
        get() = signedChangePrice?.let { DecimalFormat("#,###").format(it) } ?: "0"

    val formattedChangeRate : String
        get() = changeRate?.let { DecimalFormat("#,###").format(it) } ?: "0"


}