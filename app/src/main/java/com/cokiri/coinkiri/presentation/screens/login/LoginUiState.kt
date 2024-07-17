package com.cokiri.coinkiri.presentation.screens.login

sealed interface LoginUiState {
    object LogInSuccess : LoginUiState  // 로그인 성공
    object LogInFail : LoginUiState     // 로그인 실패
    object Loading : LoginUiState
    object Initial : LoginUiState
}