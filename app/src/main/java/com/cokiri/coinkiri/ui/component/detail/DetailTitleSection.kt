package com.cokiri.coinkiri.ui.component.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.cokiri.coinkiri.data.remote.model.post.community.PostDetailResponseDto
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont
import com.cokiri.coinkiri.util.byteArrayToPainter
import com.cokiri.coinkiri.util.formattedDate

/**
 * 게시글(분석,커뮤니티) 상세화면의 제목 섹션
 */
@Composable
fun DetailTitleSection(
    postDetailResponseDto: PostDetailResponseDto
) {

    val title = postDetailResponseDto.title
    val name = postDetailResponseDto.memberNickname
    val level = postDetailResponseDto.memberLevel
    val profileImageByteArray = postDetailResponseDto.memberPic
    val profileImage = byteArrayToPainter(profileImageByteArray)
    val createDate = formattedDate(postDetailResponseDto.createdAt)

    Column(
        modifier = Modifier
            .background(CoinkiriWhite)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .background(CoinkiriWhite)
        ) {
            Text(
                text = title,
                fontFamily = PretendardFont,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier.padding(10.dp),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoinkiriWhite)
                .padding(bottom = 5.dp)
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.size(5.dp))
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    Image(
                        painter = profileImage,
                        contentScale = ContentScale.Crop,
                        contentDescription = "프로필 사진",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.size(5.dp))
                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(
                        text = "Lv.$level $name",
                        fontFamily = PretendardFont,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = createDate,
                        fontFamily = PretendardFont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                }
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(CoinkiriPointGreen),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(10.dp)
            ) {
                Text("팔로우")
            }
        }
    }
}