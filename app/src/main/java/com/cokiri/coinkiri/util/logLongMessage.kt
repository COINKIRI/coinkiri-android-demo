package com.cokiri.coinkiri.util

import android.util.Log

const val MAX_LOG_LENGTH = 4000

fun logLongMessage(tag: String, message: String) {
    if (message.length > MAX_LOG_LENGTH) {
        val chunkCount = message.length / MAX_LOG_LENGTH
        for (i in 0..chunkCount) {
            val max = MAX_LOG_LENGTH * (i + 1)
            if (max >= message.length) {
                Log.d(tag, message.substring(MAX_LOG_LENGTH * i))
            } else {
                Log.d(tag, message.substring(MAX_LOG_LENGTH * i, max))
            }
        }
    } else {
        Log.d(tag, message)
    }
}