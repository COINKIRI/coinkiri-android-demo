package com.cokiri.coinkiri.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.price.CoinDetailScreen
import com.cokiri.coinkiri.presentation.price.PriceScreen
import com.cokiri.coinkiri.util.COIN_DETAIL_SCREEN
import com.cokiri.coinkiri.util.PRICE

fun NavGraphBuilder.priceNavGraph(
    navController: NavHostController
) {
    composable(PRICE) {
        PriceScreen(navController = navController)
    }

    composable("$COIN_DETAIL_SCREEN/{coinMarketId}/{coinId}/{coinKoreaName}") { backStackEntry ->
        val coinMarketId = backStackEntry.arguments?.getString("coinMarketId") ?: return@composable
        val coinId = backStackEntry.arguments?.getString("coinId") ?: return@composable
        val coinKoreaName = backStackEntry.arguments?.getString("coinKoreaName") ?: return@composable
        CoinDetailScreen(
            navController = navController,
            coinMarketId = coinMarketId,
            coinId = coinId,
            coinKoreaName = coinKoreaName
        )
    }
}