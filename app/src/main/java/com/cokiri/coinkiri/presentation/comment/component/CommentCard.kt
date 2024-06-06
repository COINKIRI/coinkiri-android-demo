package com.cokiri.coinkiri.presentation.comment.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.data.remote.model.comment.CommentList
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.util.byteArrayToPainter

@Composable
fun CommentCard(
    comment: CommentList
) {

    val level = comment.member.level
    val name = comment.member.nickname
    val data = comment.createdAt
    val content = comment.content
    val profileImageByteArray = comment.member.pic
    val profileImage = byteArrayToPainter(profileImageByteArray)


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp),
        colors = CardDefaults.cardColors(CoinkiriWhite)
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(5.dp),
            ) {
                Image(
                    painter = profileImage,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(40.dp)
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier
                    .padding(start = 5.dp, bottom = 3.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    text = "Lv.$level $name $data",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
                Text(
                    text = content,
                    fontSize = 13.sp
                )
            }
        }
        HorizontalDivider()
    }
}