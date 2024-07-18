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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisResponseDto
import com.cokiri.coinkiri.presentation.screens.analysis.AnalysisViewModel
import com.cokiri.coinkiri.presentation.screens.analysis.component.AnalysisListItemCard
import com.cokiri.coinkiri.util.ANALYSIS_DETAIL_SCREEN

@Composable
fun AnalysisMemberItem(
    analysisLikeList: List<AnalysisResponseDto>,
    analysisViewModel: AnalysisViewModel,
    navController: NavHostController
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        if (analysisLikeList.isEmpty()) {
            Text(
                text = "분석글이 없습니다.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(analysisLikeList.size) { index ->
                    val analysis = analysisLikeList[index]
                    val postId = analysisLikeList[index].postResponseDto.id
                    AnalysisListItemCard(
                        analysisResponseDto = analysis,
                        analysisViewModel = analysisViewModel,
                        analysisCardClick = { navController.navigate("$ANALYSIS_DETAIL_SCREEN/$postId") }
                    )
                }
            }
        }
    }
}