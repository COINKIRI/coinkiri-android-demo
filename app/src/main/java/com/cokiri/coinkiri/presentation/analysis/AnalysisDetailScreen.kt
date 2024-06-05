package com.cokiri.coinkiri.presentation.analysis

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R

@Composable
fun AnalysisDetailScreen(
    navController: NavHostController,
    postId: String,
    analysisViewModel: AnalysisViewModel = hiltViewModel()
) {

    Log.d("AnalysisDetailScreen", "postId: $postId")


    LaunchedEffect(postId) {
        analysisViewModel.fetchAnalysisDetail(postId.toLong())
    }

    val analysisDetail = analysisViewModel.analysisDetail.collectAsStateWithLifecycle()
    Log.d("AnalysisDetailScreen", "analysisDetail: $analysisDetail")

    Scaffold(
        topBar = {
            AnalysisDetailTopBar(
                backClick = { navController.popBackStack() }
            )
        },
        content = {paddingValues ->
            AnalysisDetailContent(
                paddingValues = paddingValues
            )
        },

    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisDetailTopBar(
    backClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { /*TODO*/ },
        actions = { /*TODO*/ },
        navigationIcon = {
            IconButton(
                onClick = backClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back"
                )

            }
        }
    )
}


@Composable
fun AnalysisDetailContent(
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier.padding(paddingValues)
    ) {

    }
}
