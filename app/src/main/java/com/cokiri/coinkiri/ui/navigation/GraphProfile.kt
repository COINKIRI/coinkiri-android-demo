package com.cokiri.coinkiri.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.login.LoginViewModel
import com.cokiri.coinkiri.presentation.profile.ProfileScreen
import com.cokiri.coinkiri.util.PROFILE

fun NavGraphBuilder.profileNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    composable(PROFILE) {
        ProfileScreen(
            loginViewModel = loginViewModel,
            navController = navController
        )
    }
}