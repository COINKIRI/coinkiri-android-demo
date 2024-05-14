package com.cokiri.coinkiri.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.cokiri.coinkiri.presentation.login.LoginViewModel
import com.cokiri.coinkiri.util.HOME

@Composable
fun MainGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val tabs = listOf(
        BottomNavItem.Analysis,
        BottomNavItem.Post,
        BottomNavItem.Home,
        BottomNavItem.Price,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController,
                tabs = tabs
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            analysisNavGraph(navController)
            postNavGraph(navController)
            homeNavGraph(navController)
            priceNavGraph(navController)
            profileNavGraph(navController, loginViewModel)
        }
    }
}