package com.cokiri.coinkiri.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.domain.repository.KakaoLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginRepository: KakaoLoginRepository
) : ViewModel() {


    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val loginUiState = _loginUiState.asStateFlow()
    private fun kakaoLoginSuccess() { _loginUiState.tryEmit(LoginUiState.LogInSuccess) }
    private fun kakaoLoginFail() { _loginUiState.tryEmit(LoginUiState.LogInFail) }
    private fun initial() { _loginUiState.tryEmit(LoginUiState.Initial) }


    // 카카오 로그인
    fun kakaoLogin() {
        _loginUiState.value = LoginUiState.Loading
        kakaoLoginRepository.login(
            successCallback = { kakaoLoginSuccess() },
            failureCallback = { kakaoLoginFail() }
        )
    }

    // 카카오 로그아웃
    fun kakaoLogout() {
        viewModelScope.launch {
            kakaoLoginRepository.logout()
            initial()
        }
    }
}