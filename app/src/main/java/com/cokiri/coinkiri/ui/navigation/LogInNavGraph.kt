package com.cokiri.coinkiri.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.screens.login.LoginScreen
import com.cokiri.coinkiri.presentation.screens.login.LoginViewModel
import com.cokiri.coinkiri.util.LOGIN

@Composable
fun LogInNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    NavHost(
        navController = navController,
        startDestination = LOGIN
    ) {
        composable(LOGIN) {
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }
    }
}