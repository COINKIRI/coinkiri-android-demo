package com.cokiri.coinkiri.presentation.screens.profile.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisResponseDto
import com.cokiri.coinkiri.data.remote.model.post.community.CommunityResponseDto
import com.cokiri.coinkiri.presentation.screens.analysis.AnalysisViewModel
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen

@Composable
fun MemberLikedContent(
    likeList: List<CommunityResponseDto>,
    analysisLikeList: List<AnalysisResponseDto>,
    analysisViewModel: AnalysisViewModel,
    navController: NavHostController
) {

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("분석글", "게시글")

    Log.d("MemberLikedContent", "likeList: $likeList")

    Column {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                Button(
                    onClick = { selectedTabIndex = index },
                    colors = ButtonDefaults.buttonColors(if (selectedTabIndex == index) CoinkiriPointGreen else Color.LightGray)
                ) {
                    Text(text = tab)
                }
            }
        }

        when (selectedTabIndex) {
            0 -> AnalysisMemberItem(
                analysisLikeList,
                analysisViewModel,
                navController
            )
            1 -> PostMemberItem(
                likeList,
                navController
            )
        }
    }
}