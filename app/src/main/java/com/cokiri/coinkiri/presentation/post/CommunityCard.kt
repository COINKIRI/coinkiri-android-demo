package com.cokiri.coinkiri.presentation.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriGreen
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen


@Composable
fun CommunityCard(
    onclick: () -> Unit,
){
    Card(
        onClick = onclick,
        colors = CardDefaults.cardColors(CoinkiriBackground),
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "여기 글 제목 들어갈것",
                    fontSize = 20.sp,
                )
                Text(
                    text = "(2)",
                    fontSize = 20.sp,
                    color = CoinkiriPointGreen
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(text = "유저레벨")
                    VerticalDivider(
                        thickness = 1.dp,
                        color = CoinkiriPointGreen,
                        modifier = Modifier.size(1.dp, 20.dp)
                    )
                    Text(text = "유저이름")
                    VerticalDivider(
                        thickness = 1.dp,
                        color = CoinkiriPointGreen,
                        modifier = Modifier.size(1.dp, 20.dp)
                    )
                    Text(text = "작성일")
                    VerticalDivider(
                        thickness = 1.dp,
                        color = CoinkiriPointGreen,
                        modifier = Modifier.size(1.dp, 20.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.ic_post_baseline_thumb_up),
                            contentDescription = "",
                            tint = CoinkiriPointGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = "1")
                    }
                    VerticalDivider(
                        thickness = 1.dp,
                        color = CoinkiriPointGreen,
                        modifier = Modifier.size(1.dp, 20.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.ic_post_baseline_visibility),
                            contentDescription = "",
                            tint = CoinkiriPointGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = "1")
                    }
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = CoinkiriGreen
            )
        }
    }
}


@Preview
@Composable
fun CommunityCardPreview(){
    CommunityCard(onclick = {})
}