package com.cokiri.coinkiri.presentation.price

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.data.remote.model.CoinPrice
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun CoinDetail(
    coinMarketId: String,
    coinId: String,
    coinKoreaName: String,
    priceViewModel: PriceViewModel = hiltViewModel(),
    navController: NavHostController
) {

    // 특정 코인에 대한 웹소켓 연결을 시작하고, Composable이 종료될 때 연결을 중지
    DisposableEffect(coinMarketId,coinId) {
        priceViewModel.observeSingleCoinTicker(coinMarketId)
        priceViewModel.getCoinDaysInfo(coinId)

        onDispose {
            // WebSocket 연결 중지
            priceViewModel.stopWebSocketConnection()
        }
    }

    val singleCoinTicker by priceViewModel.singleCoinTicker.collectAsState()
    val coinDaysInfo by priceViewModel.coinDaysInfo.collectAsState()

    Scaffold(
        topBar = {
            CoinDetailTopBar(
                coinMarketId,
                coinKoreaName,
                backClick = { navController.popBackStack() }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .background(CoinkiriBackground)
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                CoinDetailContent(singleCoinTicker)
                CoinChartContent(coinDaysInfo)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailTopBar(
    marketName :String,
    coinKoreaName : String,
    backClick: () -> Unit
){
    CenterAlignedTopAppBar(
        modifier = Modifier
            .background(CoinkiriBackground),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(CoinkiriBackground),
        title = {
            Text(
                text = "$coinKoreaName($marketName)",
                fontSize = 17.sp,
                maxLines = 1
            ) },
        navigationIcon = {
            IconButton(onClick = backClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = ""
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
    val coinHighPrice = singleCoinTicker?.formattedHighPrice ?: "0"
    val coinLowPrice = singleCoinTicker?.formattedLowPrice ?: "0"


    Card(
        colors = CardDefaults.cardColors(CoinkiriBackground),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoinkiriBackground)
                .padding(15.dp)
        ) {
            Row {
                Text(
                    text = "₩ $coinPrice",
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.padding(3.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = coinChangeRate)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "▲ 171,000원")
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp)
            )
            Row {
                Text(
                    text = "고가 $coinHighPrice"+"원",
                    color = Color.Red
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "저가 $coinLowPrice"+"원",
                    color = Color.Blue
                )
            }
        }
    }
}



@Composable
fun CoinChartContent(
    coinDaysInfo: List<CoinPrice>
) {

    Card(
        colors = CardDefaults.cardColors(CoinkiriBackground),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoinkiriBackground)
                .padding(10.dp)
        ) {
            Row { Text(text = "차트") }
            Row {
                CoinChart(
                    coinDaysInfo,
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) }
            Row { Text(text = "2024년 05월 05일, 09시 00분") }
            Row { Text(text = "₩ 1,000,000") }
        }
    }
}

@Composable
fun CoinChart(
    coinDaysInfo: List<CoinPrice>,
    modifier: Modifier
) {

    // 차트 데이터를 역순으로 정렬
    val reversedCoinDaysInfo = coinDaysInfo.reversed()

    AndroidView(
        factory = { context ->
            val chart = LineChart(context)

            // 차트 설정
            chart.description.isEnabled = false // 차트 설명 비활성화 ( 차트 우측 하단)
            chart.legend.isEnabled = false // 차트 범례 비활성화
            chart.setTouchEnabled(true) // 차트 터치 이벤트 활성화
            chart.setPinchZoom(true) // 차트 확대/축소 활성화

            // 차트 아래 축 설정
            chart.xAxis.isEnabled = false // 차트 아래 축 비활성화
            chart.xAxis.setDrawGridLines(false) // 차트 아래 축의 격자 줄 비활성화

            // 차트 왼쪽 축 설정
            chart.axisLeft.isEnabled = false // 왼쪽 축 비활성화
            chart.axisLeft.setDrawGridLines(false) // 차트 왼쪽 축의 격자 줄 비활성화

            // 차트 오른쪽 축 설정
            chart.axisRight.axisMaximum = reversedCoinDaysInfo.maxOfOrNull { it.tradePrice.toFloat() } ?: 0f // 차트 오른쪽 축의 최대값 설정(종가 중 최대값)
            chart.axisRight.axisMinimum = reversedCoinDaysInfo.minOfOrNull { it.tradePrice.toFloat() } ?: 0f // 차트 오른쪽 축의 최소값 설정(종가 중 최소값)
            chart.axisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART) // 오른쪽 축 내부로 이동

            // 차트 색 설정
            chart.setBackgroundColor(CoinkiriBackground.toArgb()) // 차트 배경색 설정
            chart
        },
        update = { chart ->
            val entries = reversedCoinDaysInfo
                .mapIndexed { index, bitcoinResponse ->
                    Entry(index.toFloat(), bitcoinResponse.tradePrice.toFloat())
                }

            val dataSet = LineDataSet(entries, "Bitcoin Price").apply {
                color = Color.Red.toArgb()      // 데이터 선 색상 설정
                lineWidth = 1f                  // 데이터 선 굵기 설정
                setDrawValues(false)            // 데이터 값 텍스트 그리기 비활성화
                setDrawCircles(false)           // 데이터 포인트의 원형 마커 그리기 비활성화
                mode = LineDataSet.Mode.LINEAR  // 데이터 점과 데이터 점 사이의 직선 유형 설정
                setDrawFilled(true)             // 아래 영역 채우기 활성화
                fillColor = Color.Red.toArgb()  // 아래 영역의 색상 설정
            }

            val lineData = LineData(dataSet)    // 차트 데이터 설정
            chart.data = lineData               // 차트 데이터 설정
            chart.invalidate()                  // 차트 갱신
        },
        modifier = modifier
    )
}
