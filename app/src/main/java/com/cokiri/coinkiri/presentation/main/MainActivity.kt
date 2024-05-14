package com.cokiri.coinkiri.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.cokiri.coinkiri.presentation.login.LoginUiState
import com.cokiri.coinkiri.presentation.login.LoginViewModel
import com.cokiri.coinkiri.ui.navigation.LogInNavGraph
import com.cokiri.coinkiri.ui.navigation.MainGraph
import com.cokiri.coinkiri.ui.theme.CoinkiriandroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoinkiriandroidTheme {

                val loginUiState by loginViewModel.loginUiState.collectAsState()
                val navController = rememberNavController()

                when (loginUiState) {
                    is LoginUiState.LogInSuccess -> MainGraph(navController, loginViewModel)
                    else -> LogInNavGraph(navController, loginViewModel)
                }
            }
        }
    }
}
