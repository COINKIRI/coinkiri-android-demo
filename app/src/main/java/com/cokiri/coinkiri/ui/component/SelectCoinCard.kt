package com.cokiri.coinkiri.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.byteArrayToPainter

@Composable
fun SelectCoinCard(
    selectCoinList: Coin,
    onclick: (String, Painter) -> Unit,
    ) {

    val coinName = selectCoinList.koreanName
    val byteArraySymbolImage = selectCoinList.symbolImage
    val coinSymbolImage = byteArrayToPainter(byteArraySymbolImage)


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(CoinkiriBackground),
        onClick = { onclick(coinName,coinSymbolImage) } // 클릭 시 coinName 전달
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoinkiriBackground)
                .padding(horizontal = 5.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(CoinkiriBackground),
                border = BorderStroke(1.dp, Color.LightGray),
                elevation = CardDefaults.cardElevation(5.dp),
            ) {
                Image(
                    painter = coinSymbolImage,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(35.dp)
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Text(
                text = coinName,
                fontSize = 18.sp,
                fontFamily = PretendardFont,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider()
    }
}