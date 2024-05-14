package com.cokiri.coinkiri.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.PreferencesManager
import com.cokiri.coinkiri.domain.repository.KakaoLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginRepository: KakaoLoginRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {


    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val loginUiState = _loginUiState.asStateFlow()

    init {
        if (preferencesManager.isLoggedIn) {
            _loginUiState.value = LoginUiState.LogInSuccess
        }
    }

    private fun kakaoLoginSuccess() {
        _loginUiState.tryEmit(LoginUiState.LogInSuccess)
        preferencesManager.isLoggedIn = true
    }
    private fun kakaoLoginFail() {
        _loginUiState.tryEmit(LoginUiState.LogInFail)
    }
    private fun initialLogin() {
        _loginUiState.tryEmit(LoginUiState.Initial)
        preferencesManager.isLoggedIn = false
    }


    /**
     * 카카오 로그인
     * 카카오 로그인 성공 시 _loginUiState 에 LogInSuccess 상태를 전달
     * 카카오 로그인 실패 시 _loginUiState 에 LogInFail 상태를 전달
     */
    fun kakaoLogin() {
        _loginUiState.value = LoginUiState.Loading
        kakaoLoginRepository.login(
            successCallback = { kakaoLoginSuccess() },
            failureCallback = { kakaoLoginFail() }
        )
    }

    /**
     * 카카오 로그아웃
     * 카카오 로그아웃 성공 시 _loginUiState 에 Initial 상태를 전달
     */
    fun kakaoLogout() {
        viewModelScope.launch {
            kakaoLoginRepository.logout()
            initialLogin()
        }
    }

}