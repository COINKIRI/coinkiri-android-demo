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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground

@Composable
fun CoinCard(
    coin: Coin,
    priceViewModel: PriceViewModel
) {

    val coinPainter = priceViewModel.byteArrayToPainter(coin.symbolImage)

    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .padding(vertical = 2.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(CoinkiriBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(horizontal = 5.dp), // 가로 여백
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
                        painter = coinPainter,
                        contentDescription = "",
                        modifier = Modifier
                            .size(28.dp)
                            .background(CoinkiriBackground),
                        contentScale = ContentScale.Crop
                    )
                }


                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(start = 8.dp)
                ) {
                    Text(
                        // 코인 이름
                        text = coin.koreanName,
                        fontWeight = FontWeight.Thin,
                        fontSize = 10.sp,
                        lineHeight = 1.sp // 줄간격
                    )
                    Text(
                        // 코인 마켓명
                        text = coin.krwMarket,
                        fontWeight = FontWeight.Thin,
                        fontSize = 8.sp,
                        lineHeight = 1.sp // 줄간격
                    )
                }
            }


            Row(
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    // 실시간 가격
                    text = "90,000,000",
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End
                )
                Text(
                    // 전일 종가대비 현재가의 변화율
                    text = "+12.3%",
                    fontWeight = FontWeight.Thin,
                    fontSize = 10.sp,
                    textAlign = TextAlign.End,
                )
                Text(
                    // 24시간 누적 거래대금
                    text = "300백만",
                    fontWeight = FontWeight.Thin,
                    fontSize = 10.sp,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}
