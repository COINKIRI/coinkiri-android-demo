package com.cokiri.coinkiri.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cokiri.coinkiri.data.remote.model.coin.CoinInfoDetail
import com.cokiri.coinkiri.presentation.price.PriceViewModel
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriBlack
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.byteArrayToPainter

@Composable
fun CoinRankingItem(
    priceViewModel: PriceViewModel
) {
    LaunchedEffect(Unit) {
        priceViewModel.observeKrwMarketString()
    }

    val topChangeRate by priceViewModel.topGainers.collectAsStateWithLifecycle()
    val bottomChangeRate by priceViewModel.topLosers.collectAsStateWithLifecycle()

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
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
                LazyRow(
                    contentPadding = PaddingValues(5.dp),
                ) {
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
            .width(250.dp)
            .padding(10.dp),
        colors = CardDefaults.cardColors(CoinkiriBackground),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(Color.Transparent),
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    Image(
                        painter = coinSymbol,
                        contentScale = ContentScale.Crop,
                        contentDescription = "coin symbol",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$coinMarket/$coinName",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                )
                Text(
                    text = singCoinChangeRate,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                    color = singCoinChangeRateColor
                )
            }
        }
    }
}
