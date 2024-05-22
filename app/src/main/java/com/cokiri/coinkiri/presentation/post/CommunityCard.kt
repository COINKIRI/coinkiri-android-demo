package com.cokiri.coinkiri.presentation.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.util.COMMUNITYDETAIL

@Composable
fun CommunityCard(
    onclick: () -> Unit,
){
    Card(
        onClick = onclick,
        colors = CardDefaults.cardColors(CoinkiriBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = "여기 글 제목 들어갈것")
                Text(text = "(2)") // 댓글 수
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ){
                Text(text = "유저레벨")
                Text(text = "유저이름")
                Text(text = "작성일")
                Text(text = "조회수")
                Text(text = "좋아요수")
            }
        }
    }
}