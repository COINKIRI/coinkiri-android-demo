package com.cokiri.coinkiri.presentation.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisResponseDto
import com.cokiri.coinkiri.presentation.analysis.AnalysisViewModel
import com.cokiri.coinkiri.presentation.analysis.component.InvestmentOptionCard
import com.cokiri.coinkiri.presentation.home.component.CoinRankingItem
import com.cokiri.coinkiri.presentation.home.component.MemberCoinWatchlistItem
import com.cokiri.coinkiri.presentation.price.PriceViewModel
import com.cokiri.coinkiri.presentation.price.component.CoinImage
import com.cokiri.coinkiri.ui.component.LoadingContent
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.byteArrayToPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeScreen(
    priceViewModel: PriceViewModel = hiltViewModel(),
    analysisViewModel: AnalysisViewModel = hiltViewModel()
) {
    val isLoading by priceViewModel.isLoading.collectAsStateWithLifecycle()

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

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
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    isRefreshing = true
                    priceViewModel.fetchCoinWatchlist()
                    analysisViewModel.fetchAllAnalysisPostList()
                }
            ) {
                HomeContentWrapper(
                    paddingValues,
                    isLoading,
                    priceViewModel,
                    analysisViewModel
                )
            }
        }
    )

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            isRefreshing = false
        }
    }
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
    priceViewModel: PriceViewModel,
    analysisViewModel: AnalysisViewModel,
) {
    if (isLoading) {
        LoadingContent(paddingValues)
    } else {
        HomeContent(
            paddingValues,
            priceViewModel,
            analysisViewModel
        )
    }
}


@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    priceViewModel: PriceViewModel,
    analysisViewModel: AnalysisViewModel,
) {
    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            MemberCoinWatchlistItem(priceViewModel)
            WatchCoinToAnalysisItem(priceViewModel, analysisViewModel)
            CoinRankingItem(priceViewModel)
        }
    }
}


@Composable
fun WatchCoinToAnalysisItem(
    priceViewModel: PriceViewModel,
    analysisViewModel: AnalysisViewModel
) {

    val watchlistCoinIds by priceViewModel.watchlistCoinIds.collectAsStateWithLifecycle()
    Log.d("HomeScreen", "watchlistCoinIds: $watchlistCoinIds")


    val filteredAnalysisPostList = analysisViewModel.analysisPostList.collectAsStateWithLifecycle()
        .value.filter { it.coin.id in watchlistCoinIds }

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "관심코인 분석",
                fontFamily = PretendardFont,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(15.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(5.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredAnalysisPostList.size) { index ->
                    val analysisPost = filteredAnalysisPostList[index]
                    WatchCoinToAnalysisCard(
                        analysisPost = analysisPost
                    )
                }
            }
        }
    }
}

@Composable
fun WatchCoinToAnalysisCard(
    analysisPost: AnalysisResponseDto
) {

    val authorName = analysisPost.postResponseDto.memberNickname
    val analysisCoinName = analysisPost.coin.koreanName

    val investmentOption = analysisPost.investmentOption
    val targetPrice = analysisPost.targetPrice

    val coinImage = byteArrayToPainter(analysisPost.coin.symbolImage)
    val authorProfile = byteArrayToPainter(analysisPost.memberPic)


    Card(
        modifier = Modifier
            .width(350.dp)
            .padding(5.dp)
            .border(1.dp, CoinkiriBackground, RoundedCornerShape(15.dp)),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 15.dp, end = 15.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CoinImage(
                    coinPainter = authorProfile,
                    modifier = Modifier
                        .size(35.dp)
                        .background(CoinkiriWhite)
                )
                Text(
                    text = "$authorName 님의 $analysisCoinName 분석",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                CoinImage(
                    coinPainter = coinImage,
                    modifier = Modifier
                        .size(35.dp)
                        .background(CoinkiriWhite)
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .height(85.dp)
            ) {
                when (investmentOption) {
                    "강력매수" -> InvestmentOptionCard(
                        investmentOption = investmentOption,
                        colors = Color.Red
                    )

                    "매수" -> InvestmentOptionCard(
                        investmentOption = investmentOption,
                        colors = Color.Red
                    )

                    "중립" -> InvestmentOptionCard(
                        investmentOption = investmentOption,
                        colors = Color.Black
                    )

                    "매도" -> InvestmentOptionCard(
                        investmentOption = investmentOption,
                        colors = Color.Blue
                    )

                    "강력매도" -> InvestmentOptionCard(
                        investmentOption = investmentOption,
                        colors = Color.Blue
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        colors = CardDefaults.cardColors(Color.LightGray),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = targetPrice,
                                fontSize = 18.sp,
                                fontFamily = PretendardFont,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = "목표가",
                                fontSize = 12.sp,
                                fontFamily = PretendardFont,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

