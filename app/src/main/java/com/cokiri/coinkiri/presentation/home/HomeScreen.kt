package com.cokiri.coinkiri.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.data.remote.model.coin.CoinInfoDetail
import com.cokiri.coinkiri.presentation.home.component.MemberCoinWatchlistItem
import com.cokiri.coinkiri.presentation.price.PriceViewModel
import com.cokiri.coinkiri.ui.theme.CoinkiriBlack
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.byteArrayToPainter

@Composable
fun HomeScreen(
    priceViewModel: PriceViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val isLoading by priceViewModel.isLoading.collectAsStateWithLifecycle()

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
            HomeContentWrapper(
                paddingValues,
                isLoading,
                priceViewModel
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
                modifier = Modifier.size(120.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(CoinkiriWhite),
        windowInsets = TopAppBarDefaults.windowInsets
    )
}

@Composable
fun HomeContentWrapper(
    paddingValues: PaddingValues,
    isLoading: Boolean,
    priceViewModel: PriceViewModel
) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        HomeContent(paddingValues, priceViewModel)
    }
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
            MemberCoinWatchlistItem(priceViewModel)
            CoinRankingItem(priceViewModel)
        }
    }
}

@Composable
fun CoinRankingItem(
    priceViewModel: PriceViewModel
) {
    LaunchedEffect(Unit) {
        priceViewModel.observeKrwMarketString()
    }

    val topChangeRate by priceViewModel.topGainers.collectAsStateWithLifecycle()
    val bottomChangeRate by priceViewModel.topLosers.collectAsStateWithLifecycle()

    Surface(
        color = CoinkiriWhite,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(
                    text = "상승률 Top 5",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                LazyRow {
                    items(topChangeRate.size) {
                        val coin = topChangeRate[it]
                        TextCoinChangeRateCard(coin)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
            ) {
                Text(
                    text = "하락률 Top 5",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                LazyRow {
                    items(bottomChangeRate.size) {
                        val coin = bottomChangeRate[it]
                        TextCoinChangeRateCard(coin)
                    }
                }
            }
        }
    }
}

@Composable
fun TextCoinChangeRateCard(
    coin: CoinInfoDetail
) {
    val coinName = coin.coin.koreanName
    val coinMarket = coin.coin.market
    val coinSymbol = byteArrayToPainter(coin.coin.symbolImage)
    val coinChangeRate = coin.ticker?.formattedSignedChangeRate
    val singCoinChangeRate = coinChangeRate?.let {
        if (it.contains("-")) it else "+$it"
    } ?: "0.00%"
    val singCoinChangeRateColor = coinChangeRate?.let {
        if (it.contains("-")) Color.Blue else Color.Red
    } ?: CoinkiriBlack

    Card(
        modifier = Modifier
            .width(300.dp)
            .padding(15.dp),
        colors = CardDefaults.cardColors(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    Image(
                        painter = coinSymbol,
                        contentScale = ContentScale.Crop,
                        contentDescription = "coin symbol",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = coinMarket,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                )
                Text(
                    text = coinName,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.padding(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = singCoinChangeRate,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                    color = singCoinChangeRateColor
                )
            }
        }
    }
}
