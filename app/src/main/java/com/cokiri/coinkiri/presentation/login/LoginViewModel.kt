package com.cokiri.coinkiri.presentation.login

import com.cokiri.coinkiri.domain.repository.KakaoLoginRepository
import com.cokiri.coinkiri.domain.repository.UserRepository
import com.cokiri.coinkiri.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginRepository: KakaoLoginRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    // 로그인 상태를 담는 StateFlow 객체 생성(초기값은 LoginUiState.Initial)
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val loginUiState: StateFlow<LoginUiState> get() = _loginUiState

    // 초기에 로그인 상태 확인을 위한 init 블록
    init { checkLoginStatus() }

    /**
     * 로그인 상태 확인을 위한 함수
     */
    private fun checkLoginStatus() {
        if (userRepository.isLoggedIn()) {
            loginSuccess()
        } else {
            loginFail()
        }
    }

    private fun kakaoSignUpSuccess(accessToken: String) {
        _loginUiState.value = LoginUiState.Loading
        executeWithLoading(
            block = { userRepository.signUpUser(accessToken, "KAKAO") },
            onSuccess = { loginSuccess() },
            onFailure = { loginFail() }
        )
    }

    fun kakaoLogin() {
        _loginUiState.value = LoginUiState.Loading
        kakaoLoginRepository.login(
            successCallback = { accessToken -> kakaoSignUpSuccess(accessToken) },
            failureCallback = { loginFail() }
        )
    }

    fun kakaoLogout() {
        executeWithLoading(
            block = {
                userRepository.logoutUser()
                kakaoLoginRepository.logout()
                Result.success(Unit) // 로그아웃에 성공한 경우 Result.success를 반환
            },
            onSuccess = { initialLogin() }
        )
    }

    private fun loginSuccess() { _loginUiState.tryEmit(LoginUiState.LogInSuccess) }
    private fun loginFail() { _loginUiState.tryEmit(LoginUiState.LogInFail) }
    private fun initialLogin() { _loginUiState.tryEmit(LoginUiState.Initial) }
}
