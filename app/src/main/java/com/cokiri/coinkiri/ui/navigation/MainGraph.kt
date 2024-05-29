package com.cokiri.coinkiri.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cokiri.coinkiri.presentation.login.LoginViewModel
import com.cokiri.coinkiri.presentation.post.PostViewModel
import com.cokiri.coinkiri.util.ANALYSIS
import com.cokiri.coinkiri.util.HOME
import com.cokiri.coinkiri.util.POST
import com.cokiri.coinkiri.util.PRICE
import com.cokiri.coinkiri.util.PROFILE

@Composable
fun MainGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    postViewModel: PostViewModel
) {
    val tabs = listOf(
        BottomNavItem.Analysis,
        BottomNavItem.Post,
        BottomNavItem.Home,
        BottomNavItem.Price,
        BottomNavItem.Profile
    )

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    // 현재 화면이 바텀 바를 보여줘야 하는지 여부를 결정
    val shouldShowBottomBar = currentDestination in listOf(
        ANALYSIS,
        POST,
        HOME,
        PRICE,
        PROFILE
    )

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavBar(
                    navController = navController,
                    tabs = tabs
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            analysisNavGraph(navController)
            postNavGraph(navController, postViewModel)
            homeNavGraph(navController)
            priceNavGraph(navController)
            profileNavGraph(navController, loginViewModel)
        }
    }
}