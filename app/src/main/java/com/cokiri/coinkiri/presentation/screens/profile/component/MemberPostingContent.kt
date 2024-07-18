package com.cokiri.coinkiri.presentation.screens.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.presentation.screens.analysis.AnalysisViewModel
import com.cokiri.coinkiri.presentation.screens.post.PostViewModel
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen

@Composable
fun MemberPostingContent(
    navController: NavHostController,
    postViewModel: PostViewModel,
    analysisViewModel: AnalysisViewModel
) {
    LaunchedEffect(Unit) {
        postViewModel.fetchUserCommunityList()
    }

    LaunchedEffect(Unit) {
        analysisViewModel.fetchUserAnalysisList()
    }

    val isLoading by postViewModel.isLoading.collectAsStateWithLifecycle()
    val userList by postViewModel.userCommunity.collectAsStateWithLifecycle()

    val userAnalysisList by analysisViewModel.userAnalysisList.collectAsStateWithLifecycle()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("분석글", "게시글")

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

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            when (selectedTabIndex) {
                0 -> AnalysisMemberItem(
                    userAnalysisList,
                    analysisViewModel,
                    navController
                )
                1 -> PostMemberItem(
                    userList,
                    navController
                )
            }
        }
    }
}