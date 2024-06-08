package com.cokiri.coinkiri.presentation.price

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.data.remote.model.coin.CoinInfoDetail
import com.cokiri.coinkiri.presentation.price.component.CoinItem
import com.cokiri.coinkiri.ui.component.ErrorContent
import com.cokiri.coinkiri.ui.component.LoadingContent
import com.cokiri.coinkiri.ui.theme.CoinkiriBlack
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.util.COIN_DETAIL_SCREEN

@Composable
fun PriceScreen(
    priceViewModel: PriceViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val coinInfoDetailList by priceViewModel.coinInfoDetailList.collectAsState()
    val isLoading by priceViewModel.isLoading.collectAsState()
    val errorMessage by priceViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        priceViewModel.observeKrwMarketString()
    }

    Scaffold(
        topBar = {
            PriceTopBar(title = "시세조회", onSearchClicked = { /*TODO*/ })
        },
        content = { paddingValues ->
            when {
                isLoading -> LoadingContent(paddingValues)
                errorMessage != null -> ErrorContent(paddingValues, errorMessage)
                else -> PriceContent(
                    coinInfoDetailList = coinInfoDetailList,
                    paddingValues = paddingValues,
                    onCoinClicked = { coinMarket, coinId, coinKoreanName ->
                        priceViewModel.stopWebSocketConnection()
                        navController.navigate("$COIN_DETAIL_SCREEN/$coinMarket/$coinId/$coinKoreanName")
                    }
                )
            }
        }
    )
}


/**
 * 시세 조회 화면의 TopAppBar를 표시하는 Composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceTopBar(
    title: String,
    onSearchClicked: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CoinkiriWhite,
            titleContentColor = CoinkiriBlack,
        ),
        title = {
            Text(text = title)
        },
        actions = {
            IconButton(onClick = onSearchClicked) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            }
        }
    )
}


/**
 * 시세 조회 화면의 Content를 표시하는 Composable
 * @param coinInfoDetailList 시세 조회 화면에 표시할 코인 정보 리스트
 * @param paddingValues PaddingValues
 * @param onCoinClicked 코인 클릭 시 호출되는 함수 (코인 시세 조회 화면으로 이동)
 */
@Composable
fun PriceContent(
    coinInfoDetailList: List<CoinInfoDetail>,
    paddingValues: PaddingValues,
    onCoinClicked: (String, Long, String) -> Unit
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        LazyColumn {
            items(coinInfoDetailList.size) { index ->
                val coinInfoDetail = coinInfoDetailList[index]
                val coinMarket = coinInfoDetail.coin.krwMarket
                val coinKoreanName = coinInfoDetail.coin.koreanName
                val coinId = coinInfoDetail.coin.coinId
                CoinItem(
                    coinInfoDetail = coinInfoDetail,
                    onClick = {
                        onCoinClicked(coinMarket, coinId, coinKoreanName)
                    }
                )
            }
        }
    }
}