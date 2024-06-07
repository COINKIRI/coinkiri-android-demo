package com.cokiri.coinkiri.presentation.price

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont

@Composable
fun CoinDetailScreen(
    coinMarketId: String,
    coinId: String,
    coinKoreaName: String,
    priceViewModel: PriceViewModel = hiltViewModel(),
    navController: NavHostController
) {
    // 특정 코인에 대한 웹소켓 연결을 시작하고, Composable이 종료될 때 연결을 중지
    DisposableEffect(coinMarketId, coinId) {
        priceViewModel.observeSingleCoinTicker(coinMarketId)
        priceViewModel.getCoinDaysInfo(coinId)
        priceViewModel.checkIfCoinInWatchlist(coinId.toLong())

        onDispose {
            // WebSocket 연결 중지
            priceViewModel.stopWebSocketConnection()
        }
    }

    val isLoading by priceViewModel.isLoading.collectAsState()
    val singleCoinTicker by priceViewModel.singleCoinTicker.collectAsState()
    val coinDaysInfo by priceViewModel.coinDaysInfo.collectAsState()
    val isCoinInWatchlist by priceViewModel.isCoinInWatchlist.collectAsState()
    val coinTalkList by priceViewModel.coinTalkList.collectAsStateWithLifecycle()
    val coinTalkContent by priceViewModel.coinTalkContent.collectAsStateWithLifecycle()

    val tabs = remember { listOf("차트", "코인톡") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            CoinDetailTopBar(
                coinMarketId,
                coinKoreaName,
                isCoinInWatchlist,
                backClick = { navController.popBackStack() },
                addCoinToWatchlistClick = {
                    if (isCoinInWatchlist) {
                        priceViewModel.deleteCoinFromWatchlist(coinId.toLong())
                    } else {
                        priceViewModel.addCoinToWatchlist(coinId.toLong())
                    }
                }
            )
        },
        content = { paddingValues ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CoinkiriWhite)
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .background(CoinkiriWhite)
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    CoinDetailContent(singleCoinTicker)
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = TabRowDefaults.primaryContainerColor,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(title) }
                            )
                        }
                    }
                    when (selectedTabIndex) {
                        0 -> CoinChartScreen(coinDaysInfo, singleCoinTicker)
                        1 -> CoinTalkScreen(
                            coinId = coinId.toLong(),
                            coinTalkList = coinTalkList,
                            coinTalkContent = coinTalkContent,
                            onCoinTalkContentChanged = priceViewModel::onCoinTalkContentChanged,
                            onSubmitClick = {
                                priceViewModel.submitCoinTalk(coinId.toLong())
                                priceViewModel.onCoinTalkContentChanged("")
                            }
                        )
                    }
                }
            }
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailTopBar(
    marketName: String,
    coinKoreaName: String,
    isCoinInWatchlist: Boolean,
    backClick: () -> Unit,
    addCoinToWatchlistClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.background(CoinkiriWhite),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(CoinkiriWhite),
        title = {
            Text(
                text = "$coinKoreaName($marketName)",
                fontFamily = PretendardFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                maxLines = 1
            )
        },
        navigationIcon = {
            IconButton(onClick = backClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = addCoinToWatchlistClick) {
                Icon(
                    painter = if (isCoinInWatchlist) painterResource(R.drawable.ic_coin_star_fill)
                    else painterResource(R.drawable.ic_coin_star_outline),
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .size(35.dp),
                    tint = if (isCoinInWatchlist) CoinkiriPointGreen else Color.Black
                )
            }
        }
    )
}

@Composable
fun CoinDetailContent(
    singleCoinTicker: Ticker?
) {
    val coinPrice = singleCoinTicker?.formattedTradePrice ?: "0"
    val coinChangeRate = singleCoinTicker?.formattedSignedChangeRate ?: "0"
    val coinSignedChangePrice = singleCoinTicker?.formattedSignedChangePrice ?: "0"
    val coinHighPrice = singleCoinTicker?.formattedHighPrice ?: "0"
    val coinLowPrice = singleCoinTicker?.formattedLowPrice ?: "0"

    val coinChangeRateColor = if (coinChangeRate.startsWith("-")) Color.Blue else Color.Red
    val coinChangeRateSign = if (coinChangeRate.startsWith("-")) "▼" else "▲ +"
    val coinSignedChangePriceColor = if (coinSignedChangePrice.startsWith("-")) Color.Blue else Color.Red
    val coinSignedChangePriceSign = if (coinSignedChangePrice.startsWith("-")) "" else "+"


    Card(
        colors = CardDefaults.cardColors(CoinkiriWhite),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoinkiriWhite)
                .padding(15.dp)
        ) {
            Row {
                Text(
                    text = "₩ $coinPrice",
                    fontSize = 24.sp,
                    fontFamily = PretendardFont
                )
            }
            Spacer(modifier = Modifier.padding(3.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = coinSignedChangePriceSign + coinChangeRate,
                    color = coinChangeRateColor,
                    fontFamily = PretendardFont
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "$coinChangeRateSign$coinSignedChangePrice",
                    color = coinSignedChangePriceColor,
                    fontFamily = PretendardFont
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp)
            )
            Row {
                Text(
                    text = "고가 $coinHighPrice 원",
                    color = Color.Red,
                    fontFamily = PretendardFont
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "저가 $coinLowPrice 원",
                    color = Color.Blue,
                    fontFamily = PretendardFont
                )
            }
        }
    }
}


