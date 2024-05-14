package com.cokiri.coinkiri.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.post.PostScreen
import com.cokiri.coinkiri.util.POST

fun NavGraphBuilder.postNavGraph(
    navController: NavHostController
) {
    composable(POST) {
        PostScreen()
    }
}