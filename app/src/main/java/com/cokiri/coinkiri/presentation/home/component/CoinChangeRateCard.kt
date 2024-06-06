package com.cokiri.coinkiri.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.data.remote.model.coin.CoinInfoDetail
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.byteArrayToPainter

@Composable
fun CoinChangeRateCard(
    coinInfoDetail: CoinInfoDetail
) {

    val coinName = coinInfoDetail.coin.koreanName
    val coinMarket = coinInfoDetail.coin.market
    val byteArrayCoinSymbol = coinInfoDetail.coin.symbolImage
    val coinSymbol = byteArrayToPainter(byteArrayCoinSymbol)
    val coinChangeRate = coinInfoDetail.ticker?.formattedChangeRate

    Card(
        modifier = Modifier
            .height(180.dp)
            .padding(15.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite),
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    Image(
                        painter = coinSymbol,
                        contentScale = ContentScale.Crop,
                        contentDescription = "coin symbol",
                        modifier = Modifier
                            .size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = coinMarket)
                Text(text = coinName)
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = coinChangeRate ?: "0.00%",
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }
        }
    }
}