package com.cokiri.coinkiri.extensions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * ViewModel에서 비동기 작업을 수행하면서 로딩 상태와 에러 메시지를 관리하는 확장 함수
 * @param isLoading 로딩 상태를 관리하는 MutableStateFlow
 * @param errorMessage 에러 메시지를 관리하는 MutableStateFlow
 * @param block 비동기 작업을 수행하는 람다 함수
 */
suspend fun <T> ViewModel.executeWithLoading(
    isLoading: MutableStateFlow<Boolean>,
    errorMessage: MutableStateFlow<String?>,
    block: suspend () -> Result<T>
): T? {
    isLoading.value = true
    errorMessage.value = null
    return try {
        val result = block()
        if (result.isSuccess) {
            result.getOrNull()
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error")
        }
    } catch (e: Exception) {
        errorMessage.value = e.message
        null
    } finally {
        isLoading.value = false
    }
}