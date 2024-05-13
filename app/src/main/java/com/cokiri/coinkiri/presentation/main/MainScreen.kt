package com.cokiri.coinkiri.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cokiri.coinkiri.ui.navigation.BottomNavigation
import com.cokiri.coinkiri.ui.navigation.NavigationGraph

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {
        Column(modifier = Modifier.padding(it)) {
            NavigationGraph(navController = navController)
        }
    }
}

