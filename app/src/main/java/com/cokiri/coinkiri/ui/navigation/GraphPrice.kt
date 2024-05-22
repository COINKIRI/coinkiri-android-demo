package com.cokiri.coinkiri.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.price.PriceScreen
import com.cokiri.coinkiri.util.PRICE

fun NavGraphBuilder.priceNavGraph(
    navController: NavHostController
) {
    composable(PRICE) {
        PriceScreen(

        )
    }
}