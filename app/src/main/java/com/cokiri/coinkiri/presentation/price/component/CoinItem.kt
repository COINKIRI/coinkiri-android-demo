package com.cokiri.coinkiri.presentation.price.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.data.remote.model.coin.CoinInfoDetail
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.byteArrayToPainter
import kotlinx.coroutines.delay

/**
 * 코인 정보를 표시하는 Composable
 * 코인 이미지, 코인 정보, 가격, 변동률, 거래량을 표시
 */
@Composable
fun CoinItem(
    coinInfoDetail: CoinInfoDetail,
    onClick: () -> Unit
) {
    val coinPainter = remember { byteArrayToPainter(coinInfoDetail.coin.symbolImage) }

    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 3.dp, vertical = 2.dp)
            .height(60.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(CoinkiriWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                // 코인 이미지를 표시하는 Composable 추가
                CoinImage(
                    coinPainter,
                    Modifier
                        .size(30.dp)
                        .background(CoinkiriWhite),
                    )
                Spacer(modifier = Modifier.width(8.dp))
                // 코인 정보를 표시하는 Composable 추가
                CoinInfo(coinInfoDetail)
            }

            // 가격을 표시하는 Composable 추가
            CoinPriceInfo(
                coinInfoDetail,
                Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )

            // 변동률을 표시하는 Composable 추가
            CoinChange(
                coinInfoDetail,
                Modifier
                    .align(Alignment.CenterVertically)
                    .weight(0.5f)
            )

            Spacer(modifier = Modifier.width(15.dp))

            // 거래량을 표시하는 Composable 추가
            CoinVolume(
                coinInfoDetail,
                Modifier
                    .align(Alignment.CenterVertically)
                    .weight(0.5f)
            )
        }
    }
}


/**
 * 코인 이미지를 표시하는 Composable
 */
@Composable
fun CoinImage(
    coinPainter: Painter,
    modifier: Modifier
) {
    Card(
        shape = CircleShape,
        border = BorderStroke(1.dp, color = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Image(
            painter = coinPainter,
            contentDescription = "coin symbol image",
            modifier = modifier,

            contentScale = ContentScale.Crop
        )
    }
}


/**
 * 코인 정보를 표시하는 Composable
 * 코인 이름과 마켓명을 표시
 */
@Composable
fun CoinInfo(coinInfoDetail: CoinInfoDetail) {
    Column(
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = coinInfoDetail.coin.koreanName,
            fontWeight = FontWeight.Medium,
            fontFamily = PretendardFont,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = coinInfoDetail.coin.krwMarket,
            fontWeight = FontWeight.SemiBold,
            fontFamily = PretendardFont,
            fontSize = 9.sp,
            color = Color.Gray,
            lineHeight = 1.sp
        )
    }
}


/**
 * 가격을 표시하는 Composable
 * 가격이 변동되면 텍스트 크기를 애니메이션으로 변경
 */
@Composable
fun CoinPriceInfo(
    coinInfoDetail: CoinInfoDetail,
    modifier: Modifier = Modifier
) {
    val changeRate = coinInfoDetail.ticker?.signedChangeRate ?: 0.0
    val targetColor = if (changeRate >= 0) Color.Red else Color.Blue

    // 이전 가격을 저장할 상태
    var previousPrice by remember { mutableDoubleStateOf(coinInfoDetail.ticker?.tradePrice ?: 0.0) }
    val currentPrice = coinInfoDetail.ticker?.tradePrice ?: 0.0

    // 텍스트 크기를 애니메이션으로 변경할 상태
    var targetTextSize by remember { mutableStateOf(13.sp) }

    LaunchedEffect(currentPrice) {
        if (currentPrice != previousPrice) {
            // 가격이 변동되면 텍스트 크기를 크게 변경
            targetTextSize = 14.sp
            delay(500) // 0.5초 후에 원래 크기로 복귀
            targetTextSize = 13.sp
            previousPrice = currentPrice
        }
    }

    val animatedTextSize by animateDpAsState(
        targetValue = targetTextSize.value.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = ""
    )

    Text(
        text = coinInfoDetail.ticker?.formattedTradePrice ?: "N/A",
        fontWeight = FontWeight.Bold,
        fontFamily = PretendardFont,
        fontSize = animatedTextSize.value.sp,
        textAlign = TextAlign.Center,
        color = targetColor,
        modifier = modifier
    )
}


/**
 * 변동률을 표시하는 Composable
 */
@Composable
fun CoinChange(
    coinInfoDetail: CoinInfoDetail,
    modifier: Modifier = Modifier
) {
    val changeRate = coinInfoDetail.ticker?.signedChangeRate ?: 0.0
    val changeRateColor = if (changeRate >= 0) Color.Red else Color.Blue

    Text(
        text = coinInfoDetail.ticker?.formattedSignedChangeRate ?: "N/A",
        fontWeight = FontWeight.SemiBold,
        fontFamily = PretendardFont,
        fontSize = 11.sp,
        textAlign = TextAlign.End,
        color = changeRateColor,
        modifier = modifier
    )
}


/**
 * 거래량을 표시하는 Composable
 */
@Composable
fun CoinVolume(
    coinInfoDetail: CoinInfoDetail,
    modifier: Modifier
) {
    Text(
        text = coinInfoDetail.ticker?.formattedAccTradePrice24h ?: "N/A",
        fontWeight = FontWeight.Normal,
        fontFamily = PretendardFont,
        color = Color.Gray,
        fontSize = 10.sp,
        textAlign = TextAlign.End,
        modifier = modifier
    )
}