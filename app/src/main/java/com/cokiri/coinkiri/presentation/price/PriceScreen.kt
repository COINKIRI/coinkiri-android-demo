package com.cokiri.coinkiri.presentation.price

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceScreen() {

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
        content = {
            Column(modifier = Modifier.padding(it)) {
                LazyColumn {
                    items(30) {
                        CoinCard()
                    }
                }
            }
        }
    )
}


@Composable
fun CoinCard() {
    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(CoinkiriBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(horizontal = 10.dp), // 가로 여백
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .width(IntrinsicSize.Max)
            ) {
                Card(
                    shape = CircleShape,
                    border = BorderStroke(1.dp, color = Color.LightGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "",
                        modifier = Modifier
                            .size(35.dp)
                            .background(CoinkiriBackground),
                        contentScale = ContentScale.Crop
                    )
                }


                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(start = 10.dp)
                ) {
                    Text(
                        // 코인 이름
                        text = "비트코인",
                        fontWeight = FontWeight.Thin,
                        fontSize = 10.sp,
                        lineHeight = 1.sp // 줄간격
                    )
                    Text(
                        // 코인 마켓명
                        text = "KRW-BTC",
                        fontWeight = FontWeight.Thin,
                        fontSize = 8.sp,
                        lineHeight = 1.sp // 줄간격
                    )
                }
            }


            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    // 실시간 가격
                    text = "90,000,000",
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(end = 15.dp)
                )
                Text(
                    // 전일 종가대비 현재가의 변화율
                    text = "+12.3%",
                    fontWeight = FontWeight.Thin,
                    fontSize = 10.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .width(45.dp)
                )
                Text(
                    // 24시간 누적 거래대금
                    text = "300백만",
                    fontWeight = FontWeight.Thin,
                    fontSize = 10.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .width(60.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun PriceScreenPreview() {
    PriceScreen()
}

@Preview
@Composable
fun CoinCardPreview() {
    CoinCard()
}
