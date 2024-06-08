package com.cokiri.coinkiri.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formattedDateTime(createdAt: String): String {
    val dateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)
    return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
}


fun formattedDate(createdAt: String): String {
    val dateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)
    return dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH:mm"))
}