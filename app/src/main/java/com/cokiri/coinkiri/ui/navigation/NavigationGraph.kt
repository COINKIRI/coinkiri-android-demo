package com.cokiri.coinkiri.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.analysis.AnalysisScreen
import com.cokiri.coinkiri.presentation.home.HomeScreen
import com.cokiri.coinkiri.presentation.post.PostScreen
import com.cokiri.coinkiri.presentation.price.PriceScreen
import com.cokiri.coinkiri.presentation.profile.ProfileScreen

@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route
    ){
        composable(BottomNavItem.Analysis.route){
            AnalysisScreen()
        }
        composable(BottomNavItem.Post.route){
            PostScreen()
        }
        composable(BottomNavItem.Home.route){
            HomeScreen()
        }
        composable(BottomNavItem.Price.route){
            PriceScreen()
        }
        composable(BottomNavItem.Profile.route){
            ProfileScreen()
        }
    }
}