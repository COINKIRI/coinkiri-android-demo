package com.cokiri.coinkiri.presentation.price

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriBlack
import com.cokiri.coinkiri.util.COIN_DETAIL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceScreen(
    priceViewModel: PriceViewModel = hiltViewModel(),
    navController: NavHostController
) {

    LaunchedEffect(Unit) {
        // WebSocket 연결 시작
        priceViewModel.observeKrwMarketString()
    }

    val coinInfoDetailList by priceViewModel.coinInfoDetailList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CoinkiriBackground,
                    titleContentColor = CoinkiriBlack,
                ),
                title = {
                    Text(text = "시세조회")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                LazyColumn {
                    items(coinInfoDetailList.size) { index ->
                        val coinInfoDetail = coinInfoDetailList[index]
                        val coinMarket = coinInfoDetail.coin.krwMarket
                        val coinKoreanName = coinInfoDetail.coin.koreanName
                        val coinId = coinInfoDetail.coin.coinId
                        CoinCard(
                            coinInfoDetail = coinInfoDetail,
                            onClick = {
                                priceViewModel.stopWebSocketConnection()
                                navController.navigate("$COIN_DETAIL/$coinMarket/$coinId/$coinKoreanName")
                            }

                        )
                    }
                }
            }
        }
    )
}