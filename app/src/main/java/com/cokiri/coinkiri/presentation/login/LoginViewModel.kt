package com.cokiri.coinkiri.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.domain.repository.KakaoLoginRepository
import com.cokiri.coinkiri.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginRepository: KakaoLoginRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    // 로그인 상태를 담는 StateFlow 객체 생성(초기값은 LoginUiState.Initial)
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)

    // loginUiState를 StateFlow로 변환하여 외부에서 접근할 수 있도록 설정
    val loginUiState = _loginUiState.asStateFlow()

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
        viewModelScope.launch {
            val result = userRepository.signUpUser(accessToken, "KAKAO")
            if (result.isSuccess) {
                loginSuccess()
            } else {
                loginFail()
            }
        }
    }


    fun kakaoLogin() {
        _loginUiState.value = LoginUiState.Loading
        kakaoLoginRepository.login(
            successCallback = { accessToken -> kakaoSignUpSuccess(accessToken) },
            failureCallback = { loginFail() }
        )
    }

    fun kakaoLogout() {
        viewModelScope.launch {
            userRepository.logoutUser()
            kakaoLoginRepository.logout()
            initialLogin()
        }
    }


    private fun loginSuccess() { _loginUiState.tryEmit(LoginUiState.LogInSuccess) } // 로그인 성공 시 호출 함수(LoginUiStated를 LogInSuccess로 상태 변경)
    private fun loginFail() { _loginUiState.tryEmit(LoginUiState.LogInFail) }       // 로그인 실패 시 호출 함수(LoginUiStated를 LogInFail로 상태 변경)
    private fun initialLogin() { _loginUiState.tryEmit(LoginUiState.Initial) }      // 초기 로그인 상태로 변경 함수(LoginUiStated를 Initial로 상태 변경)
}