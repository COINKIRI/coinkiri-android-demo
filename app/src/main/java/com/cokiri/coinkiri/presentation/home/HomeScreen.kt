package com.cokiri.coinkiri.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.presentation.home.component.CoinChangeRateCard
import com.cokiri.coinkiri.presentation.home.component.MemberCoinWatchlistCard
import com.cokiri.coinkiri.presentation.price.PriceViewModel
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite

@Composable
fun HomeScreen(
    priceViewModel: PriceViewModel = hiltViewModel(),
    navController: NavHostController,

    ) {

    LaunchedEffect(Unit) {
        priceViewModel.fetchCoinWatchlist()
    }

    DisposableEffect(Unit) {
        onDispose {
            priceViewModel.stopWebSocketConnection()
        }
    }

    Scaffold(
        topBar = {
            HomeTopBar()
        },
        content = { paddingValues ->
            HomeContent(
                paddingValues,
                priceViewModel,
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_coinkrir),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(CoinkiriWhite),
        windowInsets = TopAppBarDefaults.windowInsets
    )
}


@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    priceViewModel: PriceViewModel,
) {
    LazyColumn(
        contentPadding = paddingValues
    ) {
        item {
//            Top5RisingCoins(
//                priceViewModel
//            )
            MemberCoinWatchlistCard(
                priceViewModel
            )
        }
    }
}


@Composable
fun Top5RisingCoins(
    priceViewModel: PriceViewModel
) {

    LaunchedEffect(Unit) {
        priceViewModel.observeKrwMarketString()
    }

    DisposableEffect(Unit) {
        onDispose {
            priceViewModel.stopWebSocketConnection()
        }
    }

    val topChangeRate = priceViewModel.topGainers.collectAsState().value
    val bottomChangeRate = priceViewModel.topLosers.collectAsState().value


    LazyRow {
        items(topChangeRate.size) {
            val coin = topChangeRate[it]
            CoinChangeRateCard(
                coinInfoDetail = coin
            )
        }
    }

    LazyRow {
        items(bottomChangeRate.size) {
            val coin = topChangeRate[it]
            CoinChangeRateCard(
                coinInfoDetail = coin
            )
        }
    }
}