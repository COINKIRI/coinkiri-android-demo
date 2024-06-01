package com.cokiri.coinkiri.presentation.analysis.component

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground

/**
 * 분석글 목록에서 사용되는 분석글 카드
 */
@Composable
fun AnalysisListItemCard() {
    Card(
        onClick = { /*TODO*/ },
        colors =  CardDefaults.cardColors(CoinkiriBackground),
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
            ) {
                Card(
                    shape = CircleShape,
                    border = BorderStroke(1.dp, color = Color.LightGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .background(CoinkiriBackground),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 5.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(text = "코인종목명")
                    Text(text = "코인가격")
                }
            }


            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .height(120.dp)
                    .fillMaxWidth(1f)

            ) {

                SellCard()

                Column(
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 5.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        colors = CardDefaults.cardColors(Color.LightGray)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "목표가격 100,000,000")
                        }
                    }
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        colors = CardDefaults.cardColors(Color.LightGray)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "현재 기대 수익률 +16%")
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
                verticalAlignment = Alignment.CenterVertically,
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
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = "",
                            modifier = Modifier
                                .size(35.dp)
                                .background(CoinkiriBackground),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                    Text(text = "작성자 닉네임")
                }

                Text(text = "2024.04.04 10:00")
            }

            HorizontalDivider()


            Row (
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
                    Text(text = "목표기간")
                    Text(text = "2024.10.10 까지")
                }
            }
        }
    }
}

// 매도 카드
@Preview
@Composable
fun SellCard() {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight()
            .width(90.dp),
        colors = CardDefaults.cardColors(Color.Blue)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "매도",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "투자의견",
                color = Color.White
            )
        }
    }
}

// 중립 카드
@Preview
@Composable
fun NeutralCard() {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight()
            .width(90.dp),
        colors = CardDefaults.cardColors(Color.DarkGray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "중립",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "투자의견",
                color = Color.White
            )
        }
    }
}

// 매수 카드
@Preview
@Composable
fun BuyCard() {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight()
            .width(90.dp),
        colors = CardDefaults.cardColors(Color.Red)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "매수",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "투자의견",
                color = Color.White
            )
        }
    }
}