package com.cokiri.coinkiri.presentation.price

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.data.remote.model.coin.CoinPrice
import com.cokiri.coinkiri.domain.model.Ticker
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF

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
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                Column(
                    modifier = Modifier
                        .background(CoinkiriWhite)
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    CoinDetailContent(singleCoinTicker)
                    CoinChartContent(coinDaysInfo, singleCoinTicker)
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

@Composable
fun CoinChartContent(
    coinDaysInfo: List<CoinPrice>,
    singleCoinTicker: Ticker?
) {
    val coinChangeRate = singleCoinTicker?.formattedSignedChangeRate ?: "0"
    val coinChangeRateColor = if (coinChangeRate.startsWith("-")) Color.Blue else Color.Red

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
                .padding(10.dp)
        ) {
            Surface {
                CoinChart(
                    coinDaysInfo,
                    Modifier.fillMaxSize(),
                    coinChangeRateColor // 차트 색상으로 전달
                )
            }
        }
    }
}

@Composable
fun CoinChart(
    coinDaysInfo: List<CoinPrice>,
    modifier: Modifier,
    lineColor: Color // 차트 색상을 전달받음
) {
    // 차트 데이터를 역순으로 정렬
    val reversedCoinDaysInfo = coinDaysInfo.reversed()

    var formattedDateTime by remember { mutableStateOf(coinDaysInfo.firstOrNull()?.formattedDateTime ?: "") }
    var formattedTradePrice by remember { mutableStateOf(coinDaysInfo.firstOrNull()?.formattedTradePrice ?: "") }

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoinkiriWhite)
        ) {
            Row {
                Icon(
                    painter = painterResource(R.drawable.ic_green_circle),
                    contentDescription ="",
                    tint = Color.Green,
                )
                Text(
                    text = formattedDateTime,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.padding(3.dp))
            Row {
                Text(
                    text = "₩ $formattedTradePrice",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        AndroidView(
            factory = { context ->
                val chart = LineChart(context)

                // 차트 설정
                chart.description.isEnabled = false // 차트 설명 비활성화 (차트 우측 하단)
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
                chart.axisRight.axisMaximum =
                    reversedCoinDaysInfo.maxOfOrNull { it.tradePrice.toFloat() }
                        ?: 0f // 차트 오른쪽 축의 최대값 설정(종가 중 최대값)
                chart.axisRight.axisMinimum =
                    reversedCoinDaysInfo.minOfOrNull { it.tradePrice.toFloat() }
                        ?: 0f // 차트 오른쪽 축의 최소값 설정(종가 중 최소값)
                chart.axisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART) // 오른쪽 축 내부로 이동

                // 차트 색 설정
                chart.setBackgroundColor(CoinkiriWhite.toArgb()) // 차트 배경색 설정

                // CustomMarkerView 설정
                val markerView = CustomMarkerView(context, R.layout.custom_marker_view)
                chart.marker = markerView

                // 차트 터치 이벤트 리스너 설정
                chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        e?.let {
                            val index = it.x.toInt()
                            if (index in reversedCoinDaysInfo.indices) {
                                formattedDateTime = reversedCoinDaysInfo[index].formattedDateTime
                                formattedTradePrice = reversedCoinDaysInfo[index].formattedTradePrice
                            }
                        }
                    }

                    override fun onNothingSelected() {
                        // 아무 것도 선택되지 않았을 때의 동작 (필요시 구현)
                    }
                })

                chart
            },
            update = { chart ->
                val entries = reversedCoinDaysInfo
                    .mapIndexed { index, coinPrice ->
                        Entry(index.toFloat(), coinPrice.tradePrice.toFloat())
                    }

                // 그라데이션 드로어블 생성
                val startColor = lineColor.copy(alpha = 0.3f).toArgb() // 위쪽 색상을 덜 진하게 설정 (알파 값 조정)
                val endColor = lineColor.copy(alpha = 0f).toArgb() // 아래쪽 색상 투명하게 설정

                val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(startColor, endColor)
                )

                val dataSet = LineDataSet(entries, "Coin Price").apply {
                    color = lineColor.toArgb()         // 데이터 선 색상 설정
                    lineWidth = 1f                      // 데이터 선 굵기 설정
                    setDrawValues(false)                // 데이터 값 텍스트 그리기 비활성화
                    setDrawCircles(false)               // 데이터 포인트의 원형 마커 그리기 비활성화
                    mode = LineDataSet.Mode.LINEAR      // 데이터 점과 데이터 점 사이의 직선 유형 설정
                    setDrawFilled(true)                 // 아래 영역 채우기 활성화
                    fillDrawable = gradientDrawable     // 그라데이션 드로어블 설정
                    isHighlightEnabled = true           // 강조 기능 활성화
                    highLightColor = Color.Transparent.toArgb() // 강조 선 색상 설정
                    highlightLineWidth = 0f             // 강조 선 굵기 설정
                }

                val lineData = LineData(dataSet) // 차트 데이터 설정
                chart.data = lineData // 차트 데이터 설정
                chart.invalidate() // 차트 갱신
            },
            modifier = modifier
        )
    }
}


@SuppressLint("ViewConstructor")
class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        // 마커를 업데이트하는 코드
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}