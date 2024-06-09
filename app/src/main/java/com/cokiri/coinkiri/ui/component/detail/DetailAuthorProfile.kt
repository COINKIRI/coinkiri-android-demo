package com.cokiri.coinkiri.ui.component.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.data.remote.model.post.community.PostDetailResponseDto
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.byteArrayToPainter

@Composable
fun DetailAuthorProfile(
    postDetailResponseDto: PostDetailResponseDto
) {

    val authorName = postDetailResponseDto.memberNickname
    val authorLevel = postDetailResponseDto.memberLevel
    val authorProfileImageUrl = postDetailResponseDto.memberPic
    val authorProfileImage = byteArrayToPainter(authorProfileImageUrl)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CoinkiriWhite),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .height(250.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Image(
                    painter = authorProfileImage,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(100.dp)
                )
            }
            Spacer(modifier = Modifier.size(15.dp))
            Text(
                text = "Lv.$authorLevel",
                fontFamily = PretendardFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = authorName,
                fontFamily = PretendardFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        }
    }
}