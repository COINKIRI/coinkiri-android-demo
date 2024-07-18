package com.cokiri.coinkiri.presentation.screens.profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.data.remote.model.post.community.CommunityResponseDto
import com.cokiri.coinkiri.presentation.screens.post.community.CommunityCard
import com.cokiri.coinkiri.util.COMMUNITY_DETAIL_SCREEN

@Composable
fun PostMemberItem(
    likeList: List<CommunityResponseDto>,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        if (likeList.isEmpty()) {
            Text(
                text = "게시글이 없습니다.",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(likeList.size) { index ->
                    val community = likeList[index]
                    val postId = likeList[index].postResponseDto.id
                    CommunityCard(
                        communityResponseDto = community,
                        onclick =  { navController.navigate("$COMMUNITY_DETAIL_SCREEN/$postId") },
                    )
                }
            }
        }
    }
}