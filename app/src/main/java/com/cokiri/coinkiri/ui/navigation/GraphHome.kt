package com.cokiri.coinkiri.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.screens.main.MainScreen
import com.cokiri.coinkiri.util.HOME


fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController
) {
    composable(HOME) { MainScreen() }
}