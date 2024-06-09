package com.cokiri.coinkiri.util

/**
 * 현재 가격과 목표 가격을 받아 변동률을 계산하는 함수
 */
fun calculateValueChange(currentPrice: String, targetPrice: String): String {
    // 쉼표 제거 후 숫자 변환
    val current = currentPrice.replace(",", "").toDoubleOrNull() ?: return "0%"
    val target = targetPrice.replace(",", "").toDoubleOrNull() ?: return "0%"

    // 변동률 계산
    val change = ((target - current) / current * 100).toInt()

    return when {
        change > 0 -> "+$change%"
        change < 0 -> "$change%"
        else -> "0%"
    }
}