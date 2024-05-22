package com.cokiri.coinkiri.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.post.CommunityDetail
import com.cokiri.coinkiri.presentation.post.CommunityWrite
import com.cokiri.coinkiri.presentation.post.PostScreen
import com.cokiri.coinkiri.util.COMMUNITY_DETAIL
import com.cokiri.coinkiri.util.COMMUNITY_WRITE
import com.cokiri.coinkiri.util.POST

fun NavGraphBuilder.postNavGraph(
    navController: NavHostController
) {
    composable(POST) { PostScreen(navController = navController) }
    composable(COMMUNITY_DETAIL){ CommunityDetail(navController) }
    composable(COMMUNITY_WRITE){ CommunityWrite(navController) }
}