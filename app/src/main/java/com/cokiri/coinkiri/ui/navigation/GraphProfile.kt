package com.cokiri.coinkiri.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.login.LoginViewModel
import com.cokiri.coinkiri.presentation.profile.FollowScreen
import com.cokiri.coinkiri.presentation.profile.ProfileModifyScreen
import com.cokiri.coinkiri.presentation.profile.ProfileScreen
import com.cokiri.coinkiri.util.FOLLOW
import com.cokiri.coinkiri.util.PROFILE
import com.cokiri.coinkiri.util.PROFILE_MODIFY_SCREEN

fun NavGraphBuilder.profileNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,

) {

    composable(PROFILE) {
        ProfileScreen(
            loginViewModel = loginViewModel,
            navController = navController
        )
    }

    composable(FOLLOW) { FollowScreen(navController) }


    composable(PROFILE_MODIFY_SCREEN) {
        ProfileModifyScreen(navController = navController)
    }

}