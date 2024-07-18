package com.cokiri.coinkiri.ui.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.cokiri.coinkiri.presentation.screens.analysis.AnalysisDetailScreen
import com.cokiri.coinkiri.presentation.screens.analysis.AnalysisScreen
import com.cokiri.coinkiri.presentation.screens.analysis.AnalysisViewModel
import com.cokiri.coinkiri.presentation.screens.analysis.AnalysisWriteScreen
import com.cokiri.coinkiri.presentation.screens.createpost.CreatePostScreenForAnalysis
import com.cokiri.coinkiri.util.ANALYSIS
import com.cokiri.coinkiri.util.ANALYSIS_DETAIL_SCREEN
import com.cokiri.coinkiri.util.ANALYSIS_WRITE_SCREEN
import com.cokiri.coinkiri.util.CREATE_POST_SCREEN_FOR_ANALYSIS

fun NavGraphBuilder.analysisNavGraph(
    navController: NavHostController
) {
    navigation(startDestination = ANALYSIS, route = "analysis_route") {
        composable(ANALYSIS) { AnalysisScreen(navController) }

        composable(ANALYSIS_WRITE_SCREEN) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("analysis_route")
            }
            val analysisViewModel = hiltViewModel<AnalysisViewModel>(parentEntry)
            AnalysisWriteScreen(navController = navController, analysisViewModel = analysisViewModel)
        }

        composable(CREATE_POST_SCREEN_FOR_ANALYSIS) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("analysis_route")
            }
            val analysisViewModel = hiltViewModel<AnalysisViewModel>(parentEntry)
            CreatePostScreenForAnalysis(navController = navController, analysisViewModel = analysisViewModel)
        }
    }

    composable("$ANALYSIS_DETAIL_SCREEN/{postId}") { backStackEntry ->
        val postId = backStackEntry.arguments?.getString("postId") ?: return@composable
        AnalysisDetailScreen(navController, postId)
    }
}