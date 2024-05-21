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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceScreen(
    priceViewModel: PriceViewModel = hiltViewModel()
) {

    val coinInfoDetailList by priceViewModel.coinInfoDetailList.collectAsState()
//    val coin by priceViewModel.coinList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    // TopAppBar 색상 설정
                    containerColor = CoinkiriBackground,     // AppBar 배경색을 메인 배경색으로 설정
                    titleContentColor = CoinkiriBlack,       // AppBar 컨텐츠 색을 메인 검은색으로 설정
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
                    items(coinInfoDetailList.size) {
                        CoinCard(
                            priceViewModel,
                            coinInfoDetailList[it]
                        )
                    }
                }
            }
        }
    )
}




@Preview
@Composable
fun PriceScreenPreview() {
    PriceScreen()
}
