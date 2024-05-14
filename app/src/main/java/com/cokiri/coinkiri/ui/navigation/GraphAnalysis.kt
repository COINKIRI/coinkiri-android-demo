package com.cokiri.coinkiri.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.analysis.AnalysisScreen
import com.cokiri.coinkiri.util.ANALYSIS

fun NavGraphBuilder.analysisNavGraph(
    navController: NavHostController
) {
    composable(ANALYSIS) {
        AnalysisScreen()
    }
}