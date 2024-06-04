package com.cokiri.coinkiri.presentation.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnalysisDetailScreen() {


    Scaffold(
        topBar = {
            AnalysisDetailTopBar()
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
fun AnalysisDetailTopBar() {
    CenterAlignedTopAppBar(
        title = { /*TODO*/ },
        actions = { /*TODO*/ },
        navigationIcon = { /*TODO*/ }
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
