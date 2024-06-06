package com.cokiri.coinkiri.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel의 공통 기능을 제공하는 기본 클래스
 */
open class BaseViewModel : ViewModel() {

    // 로딩 상태를 관리하는 StateFlow
    private val loadingState = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = loadingState

    // 오류 상태를 관리하는 StateFlow
    private val errorState = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = errorState


    /**
     * 비동기 작업을 실행하면서 로딩 상태와 오류 상태를 관리하는 함수
     */
    protected fun <T> executeWithLoading(
        block: suspend () -> Result<T>,
        onSuccess: (T) -> Unit,
        onFailure: (Throwable) -> Unit = { errorState.value = it.message }
    ) {
        viewModelScope.launch {
            loadingState.value = true
            errorState.value = null
            try {
                val result = block()
                if (result.isSuccess) {
                    onSuccess(result.getOrNull()!!)
                } else {
                    onFailure(result.exceptionOrNull() ?: Exception("Unknown error"))
                }
            } catch (e: Exception) {
                onFailure(e)
            } finally {
                loadingState.value = false
            }
        }
    }
}