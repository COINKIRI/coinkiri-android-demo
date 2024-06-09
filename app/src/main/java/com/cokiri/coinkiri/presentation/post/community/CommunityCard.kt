package com.cokiri.coinkiri.presentation.post.community

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.data.remote.model.post.community.CommunityResponseDto
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.CoinkiriGreen
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen


@Composable
fun CommunityCard(
    onclick: () -> Unit,
    communityResponseDto: CommunityResponseDto,
) {
    val title = communityResponseDto.postResponseDto.title
    val name = communityResponseDto.postResponseDto.memberNickname
    val level = communityResponseDto.postResponseDto.memberLevel.toString()
    val date = communityResponseDto.postResponseDto.formattedDateTime
    val viewCnt = communityResponseDto.postResponseDto.viewCnt.toString()
    val likeCnt = communityResponseDto.postResponseDto.likeCount.toString()
    val commentCnt = communityResponseDto.postResponseDto.commentCount.toString()

    Card(
        onClick = onclick,
        colors = CardDefaults.cardColors(CoinkiriWhite),
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            TitleRow(title = title, commentCnt = commentCnt)
            InfoRow(level = level, name = name, data = date, likeCnt = likeCnt, viewCnt = viewCnt)
            HorizontalDivider(thickness = 1.dp, color = CoinkiriGreen)
        }
    }
}

@Composable
fun TitleRow(title: String, commentCnt: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
        )
        Text(
            text = "($commentCnt)",
            fontSize = 20.sp,
            color = CoinkiriPointGreen
        )
    }
}

@Composable
fun InfoRow(
    level: String,
    name: String,
    data: String,
    likeCnt: String,
    viewCnt: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(text = "Lv.$level")
            Text(text = name)
            VerticalDivider(thickness = 1.dp, color = CoinkiriPointGreen,modifier = Modifier.size(1.dp, 20.dp))
            Text(text = data)
            VerticalDivider(thickness = 1.dp, color = CoinkiriPointGreen,modifier = Modifier.size(1.dp, 20.dp))
            InfoIconWithText(
                iconResId = R.drawable.ic_post_filled_thumb_up,
                text = likeCnt,
                tint = CoinkiriPointGreen,
            )
            VerticalDivider(thickness = 1.dp, color = CoinkiriPointGreen,modifier = Modifier.size(1.dp, 20.dp))
            InfoIconWithText(
                iconResId = R.drawable.ic_post_baseline_visibility,
                text = viewCnt,
                tint = CoinkiriPointGreen
            )
        }
    }
}

@Composable
fun InfoIconWithText(iconResId: Int, text: String, tint: Color) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Text(text = text)
    }
}