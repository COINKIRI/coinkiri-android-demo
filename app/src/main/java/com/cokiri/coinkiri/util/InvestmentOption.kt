package com.cokiri.coinkiri.util

/**
 * 투자 의견을 나타내는 enum 클래스
 */
enum class InvestmentOption(val value: String) {
    STRONG_BUY("강력매수"),
    BUY("매수"),
    SELL("매도"),
    HOLD("중립"),
    STRONG_SELL("강력매도");

    companion object {
        fun toEnglish(value: String): String {
            return when (value) {
                "강력매수" -> "STRONG_BUY"
                "매수" -> "BUY"
                "매도" -> "SELL"
                "중립" -> "HOLD"
                "강력매도" -> "STRONG_SELL"
                else -> "UNKNOWN"
            }
        }
    }
}