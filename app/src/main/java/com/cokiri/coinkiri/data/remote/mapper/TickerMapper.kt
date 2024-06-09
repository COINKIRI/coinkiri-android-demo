package com.cokiri.coinkiri.data.remote.mapper

import com.cokiri.coinkiri.data.remote.model.coin.UpbitTickerResponse
import com.cokiri.coinkiri.domain.model.Ticker

object TickerMapper {

    fun UpbitTickerResponseToTicker(upbitTickerResponse: UpbitTickerResponse) : Ticker{
        return Ticker(
            type = upbitTickerResponse.type,
            code = upbitTickerResponse.code,
            openingPrice = upbitTickerResponse.openingPrice,
            highPrice = upbitTickerResponse.highPrice,
            lowPrice = upbitTickerResponse.lowPrice,
            tradePrice = upbitTickerResponse.tradePrice,
            prevClosingPrice = upbitTickerResponse.prevClosingPrice,
            accTradePrice = upbitTickerResponse.accTradePrice,
            change = upbitTickerResponse.change,
            changePrice = upbitTickerResponse.changePrice,
            signedChangePrice = upbitTickerResponse.signedChangePrice,
            changeRate = upbitTickerResponse.changeRate,
            signedChangeRate = upbitTickerResponse.signedChangeRate,
            askBid = upbitTickerResponse.askBid,
            tradeVolume = upbitTickerResponse.tradeVolume,
            accTradeVolume = upbitTickerResponse.accTradeVolume,
            tradeDate = upbitTickerResponse.tradeDate,
            tradeTime = upbitTickerResponse.tradeTime,
            tradeTimestamp = upbitTickerResponse.tradeTimestamp,
            accAskVolume = upbitTickerResponse.accAskVolume,
            accBidVolume = upbitTickerResponse.accBidVolume,
            highest52WeekPrice = upbitTickerResponse.highest52WeekPrice,
            highest52WeekDate = upbitTickerResponse.highest52WeekDate,
            lowest52WeekPrice = upbitTickerResponse.lowest52WeekPrice,
            lowest52WeekDate = upbitTickerResponse.lowest52WeekDate,
            marketState = upbitTickerResponse.marketState,
            isTradingSuspended = upbitTickerResponse.isTradingSuspended,
            delistingDate = upbitTickerResponse.delistingDate,
            marketWarning = upbitTickerResponse.marketWarning,
            timestamp = upbitTickerResponse.timestamp,
            accTradePrice24h = upbitTickerResponse.accTradePrice24h,
            accTradeVolume24h = upbitTickerResponse.accTradeVolume24h,
            streamType = upbitTickerResponse.streamType
        )
    }

    fun mapToTickers(upbitTickerResponses: List<UpbitTickerResponse>) : List<Ticker>{
        return upbitTickerResponses.map { upbitTickerResponse ->
            UpbitTickerResponseToTicker(upbitTickerResponse)
        }
    }
}