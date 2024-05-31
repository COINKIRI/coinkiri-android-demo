package com.cokiri.coinkiri.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.analysis.AnalysisScreen
import com.cokiri.coinkiri.presentation.analysis.AnalysisWriteScreen
import com.cokiri.coinkiri.util.ANALYSIS
import com.cokiri.coinkiri.util.ANALYSIS_WRITE_SCREEN

fun NavGraphBuilder.analysisNavGraph(
    navController: NavHostController
) {
    composable(ANALYSIS) { AnalysisScreen(navController) }
    
    composable(ANALYSIS_WRITE_SCREEN) { AnalysisWriteScreen(navController = navController)}
}