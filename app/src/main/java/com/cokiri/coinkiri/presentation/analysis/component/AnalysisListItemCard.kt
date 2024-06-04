package com.cokiri.coinkiri.presentation.analysis.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.data.remote.model.AnalysisResponseDto
import com.cokiri.coinkiri.presentation.analysis.AnalysisViewModel
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.byteArrayToPainter
import com.cokiri.coinkiri.util.calculateValueChange

/**
 * 분석글 목록에서 사용되는 분석글 카드
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AnalysisListItemCard(
    analysisResponseDto: AnalysisResponseDto,
    analysisViewModel: AnalysisViewModel
) {

    val coinMarket = analysisResponseDto.coin.krwMarket
    val coinName = analysisResponseDto.coin.koreanName
    val byteCoinSymbolImage = analysisResponseDto.coin.symbolImage
    val coinSymbolImage = byteArrayToPainter(byteCoinSymbolImage)


    val coinPrevClosingPrice = analysisResponseDto.coinPrevClosingPrice
    val investmentOption = analysisResponseDto.investmentOption
    val targetPrice = analysisResponseDto.targetPrice
    val targetPeriod = analysisResponseDto.targetPeriod

    val memberPicByteArray = analysisResponseDto.memberPic
    val memberPic = byteArrayToPainter(memberPicByteArray)
    val memberName = analysisResponseDto.postResponseDto.memberNickname
    val formattedDate = analysisResponseDto.postResponseDto.formattedDate

    LaunchedEffect(coinMarket) {
        analysisViewModel.observeCoinTickerContinuously(coinMarket)
    }

    val coinTicker by analysisViewModel.getCoinTicker(coinMarket).collectAsState(initial = null)
    val isLoading by analysisViewModel.isLoading(coinMarket).collectAsState(initial = false)

    val coinTracePrice = coinTicker?.formattedTradePrice
    val coinSignedChangeRate = coinTicker?.formattedSignedChangeRate
    val coinSignedChangeRateColor = if (coinSignedChangeRate?.contains("-") == false) {
        Color.Red
    } else {
        Color.Blue
    }
    val coinSignedSing = if(coinSignedChangeRate?.contains("-") == false) {
        "+"
    } else {
        ""
    }

    val valueChange = coinTracePrice?.let { calculateValueChange(it, targetPrice) }
    Log.d("AnalysisListItemCard", "valueChange: $valueChange")
    val valueChangeColor = if (valueChange?.contains("-") == true) {
        Color.Blue
    } else {
        Color.Red
    }



    Card(
        onClick = { /*TODO*/ },
        colors = CardDefaults.cardColors(CoinkiriWhite),
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Card(
                    shape = CircleShape,
                    border = BorderStroke(1.dp, color = Color.LightGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Image(
                        painter = coinSymbolImage,
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .background(CoinkiriWhite),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 5.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = coinName,
                        fontFamily = PretendardFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                    )
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(12.dp))
                    } else if (coinTracePrice != null) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "₩ $coinTracePrice",
                                fontSize = 13.sp,
                                fontFamily = PretendardFont,
                                fontWeight = FontWeight.Medium
                            )
                            if (coinSignedChangeRate != null) {
                                Text(
                                    text = coinSignedSing+coinSignedChangeRate,
                                    fontSize = 13.sp,
                                    fontFamily = PretendardFont,
                                    color = coinSignedChangeRateColor
                                )
                            }
                        }
                    }
                }
            }

        }


        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .height(85.dp)
                .fillMaxWidth(1f)

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
                    shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
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
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = "목표가",
                            fontSize = 12.sp,
                            fontFamily = PretendardFont,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    colors = CardDefaults.cardColors(Color.LightGray),
                    shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (valueChange != null) {
                            Text(
                                text = valueChange,
                                fontSize = 18.sp,
                                fontFamily = PretendardFont,
                                fontWeight = FontWeight.Medium,
                                color = valueChangeColor
                            )
                        }
                        Text(
                            text = "현재 기대 수익률",
                            fontSize = 12.sp,
                            fontFamily = PretendardFont,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            Text(
                text = "분석글 내용 2줄 요약분석글 내용 2줄 요약분석글 내용 2줄 요약분석글 내용 2줄 요약분석글 내용 2줄 요약분석글 내용 2줄 요약",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Card(
                    shape = CircleShape,
                    border = BorderStroke(1.dp, color = Color.LightGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Image(
                        painter = memberPic,
                        contentDescription = "",
                        modifier = Modifier
                            .size(35.dp)
                            .background(CoinkiriWhite),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(
                    text = memberName,
                    fontSize = 15.sp,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = formattedDate,
                fontSize = 13.sp,
                fontFamily = PretendardFont,
                fontWeight = FontWeight.Normal
            )
        }

        HorizontalDivider()


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Image(
                        painter = painterResource(R.drawable.ic_post_baseline_thumb_up),
                        contentDescription = "",
                        modifier = Modifier.size(25.dp)
                    )
                }
                Text(text = "1")

                IconButton(onClick = { /*TODO*/ }) {
                    Image(
                        painter = painterResource(R.drawable.ic_post_baseline_thumb_up),
                        contentDescription = "",
                        modifier = Modifier.size(25.dp)
                    )
                }
                Text(text = "1")
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "목표기간",
                    fontSize = 13.sp,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "$targetPeriod 까지",
                    fontSize = 14.sp,
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun InvestmentOptionCard(
    investmentOption: String,
    colors: Color
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight()
            .width(90.dp),
        colors = CardDefaults.cardColors(colors),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = investmentOption,
                fontSize = 17.sp,
                fontFamily = PretendardFont,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "투자의견",
                fontFamily = PretendardFont,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp
            )
        }
    }
}
