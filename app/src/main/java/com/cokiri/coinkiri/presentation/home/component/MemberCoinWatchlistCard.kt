package com.cokiri.coinkiri.presentation.home.component

import android.graphics.drawable.GradientDrawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cokiri.coinkiri.data.remote.model.coin.WatchlistCoinPrice
import com.cokiri.coinkiri.data.remote.model.coin.WatchlistPrice
import com.cokiri.coinkiri.presentation.price.PriceViewModel
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.byteArrayToPainter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun MemberCoinWatchlistCard(
    priceViewModel: PriceViewModel
) {

    val coinWatchlist by priceViewModel.coinWatchlist.collectAsStateWithLifecycle()
    val coinWatchlistSize = coinWatchlist.size
    val coinMarketList = coinWatchlist.map { it.market }.toList()
    Log.d("ProfileScreen", "coinMarketList: $coinMarketList")

    LaunchedEffect(coinMarketList) {
        priceViewModel.getTickers(coinMarketList)
    }

    val tickers by priceViewModel.tickers.collectAsStateWithLifecycle()
    val priceList = tickers.values.map { it.formattedTradePrice } // 가격 리스트
    val formattedSignedChangePriceList =
        tickers.values.map { it.formattedSignedChangePrice } // 변동 가격 리스트
    val formattedSignedChangeRateList =
        tickers.values.map { it.formattedSignedChangeRate } // 변동률 리스트



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite),
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(5.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "내 관심종목",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = "(24 시간기준)",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Thin,
                    fontSize = 14.sp
                )
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(coinWatchlistSize) { index ->
                    val coin = coinWatchlist[index]
                    val price = priceList.getOrNull(index) ?: "N/A"
                    val changePrice = formattedSignedChangePriceList.getOrNull(index) ?: "N/A"
                    val changeRate = formattedSignedChangeRateList.getOrNull(index) ?: "N/A"
                    CoinWatchlistItem(
                        watchlistCoinPrice = coin,
                        price = price,
                        changePrice = changePrice,
                        changeRate = changeRate
                    )
                }
            }
        }
    }
}


@Composable
fun CoinWatchlistItem(
    watchlistCoinPrice: WatchlistCoinPrice,
    price: String,
    changePrice: String,
    changeRate: String
) {

    val coinName = watchlistCoinPrice.koreanName
    val byteSymbol = watchlistCoinPrice.symbolImage
    val coinSymbol = byteArrayToPainter(byteSymbol)
    val market = watchlistCoinPrice.marketName
    val coinHoursInfo = watchlistCoinPrice.coinPrices
    val changePriceColor = if (changePrice.startsWith("-")) Color.Blue else Color.Red
    val changePriceSign = if (changePrice.startsWith("-")) "" else "+"
    val changeRateSign = if (changeRate.startsWith("-")) "▼" else "▲+"

    Card(
        modifier = Modifier
            .width(250.dp)
            .height(200.dp)
            .padding(horizontal = 5.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite),
        elevation = CardDefaults.cardElevation(5.dp),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 5.dp, end = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(3.dp),
                ) {
                    Image(
                        painter = coinSymbol,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Profile Image",
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = coinName,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = market,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Thin,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = changePriceSign + changeRate,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = changePriceColor
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "$changeRateSign $changePrice",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = changePriceColor
                )
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = "₩ $price",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                )
                DayChart(
                    coinHoursInfo = coinHoursInfo,
                    lineColor = changePriceColor,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}


@Composable
fun DayChart(
    coinHoursInfo: List<WatchlistPrice>,
    modifier: Modifier,
    lineColor: Color
) {

    val reversedCoinHoursInfo = coinHoursInfo.reversed()

    AndroidView(
        factory = { context ->
            val chart = LineChart(context)

            // 차트 설정
            chart.description.isEnabled = false // 차트 설명 비활성화 (차트 우측 하단)
            chart.legend.isEnabled = false // 차트 범례 비활성화
            chart.setTouchEnabled(false) // 차트 터치 기능 비활성화
            chart.setPinchZoom(false) // 차트 확대/축소 기능 비활성화

            // 차트 아래 축 설정
            chart.xAxis.isEnabled = false // 차트 아래 축 비활성화
            chart.xAxis.setDrawGridLines(false) // 차트 아래 축의 격자 줄 비활성화

            // 차트 왼쪽 축 설정
            chart.axisLeft.isEnabled = false // 왼쪽 축 비활성화
            chart.axisLeft.setDrawGridLines(false) // 차트 왼쪽 축의 격자 줄 비활성화

            // 차트 오른쪽 축 설정
            chart.axisRight.isEnabled = false // 오른쪽 축 비활성화
            chart.axisRight.setDrawGridLines(false) // 차트 오른쪽 축의 격자 줄 비활성화


            // 차트 색 설정
            chart.setBackgroundColor(Color.Transparent.toArgb()) // 차트 배경색 설정
            chart
        },
        update = { chart ->
            val entries = reversedCoinHoursInfo
                .mapIndexed { index, coinPrice ->
                    Entry(index.toFloat(), coinPrice.tradePrice.toFloat())
                }

            //그라데이션 드로어블 생성
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
                isHighlightEnabled = false           // 강조 기능 활성화
            }

            val lineData = LineData(dataSet) // 차트 데이터 설정
            chart.data = lineData // 차트 데이터 설정
            chart.invalidate()    // 차트 갱신
        },
        modifier = modifier
    )
}