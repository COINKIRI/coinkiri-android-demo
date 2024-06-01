package com.cokiri.coinkiri.util

import java.text.NumberFormat
import java.util.Locale

fun calculateTargetPriceOptions(currentPrice: String, option: String): List<Pair<String, String>> {
    // 쉼표 제거 후 숫자 변환
    val price = currentPrice.replace(",", "").toDoubleOrNull() ?: return listOf(Pair(currentPrice, "0%"))
    val numberFormat = NumberFormat.getNumberInstance(Locale.US)

    return when (option) {
        "강력매도" -> (-50 downTo -100 step 5).map { percentage ->
            Pair(numberFormat.format((price * (1 + percentage / 100.0)).toInt()), "$percentage%")
        }
        "매도" -> (-15 downTo -50 step 5).map { percentage ->
            Pair(numberFormat.format((price * (1 + percentage / 100.0)).toInt()), "$percentage%")
        }
        "중립" -> ((-15..16).map { percentage ->
            Pair(numberFormat.format((price * (1 + percentage / 100.0)).toInt()), if (percentage > 0) "+$percentage%" else "$percentage%")
        }).distinct()
        "매수" -> (15..50 step 5).map { percentage ->
            Pair(numberFormat.format((price * (1 + percentage / 100.0)).toInt()), "+$percentage%")
        }
        "강력매수" -> (50..200 step 5).map { percentage ->
            Pair(numberFormat.format((price * (1 + percentage / 100.0)).toInt()), "+$percentage%")
        }
        else -> listOf(Pair(price.toString(), "0%"))
    }
}