package com.cokiri.coinkiri.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.post.PostScreen
import com.cokiri.coinkiri.presentation.post.PostViewModel
import com.cokiri.coinkiri.presentation.post.community.CommunityDetailScreen
import com.cokiri.coinkiri.presentation.post.community.CommunityWrite
import com.cokiri.coinkiri.util.COMMUNITY_DETAIL_SCREEN
import com.cokiri.coinkiri.util.COMMUNITY_WRITE
import com.cokiri.coinkiri.util.POST

fun NavGraphBuilder.postNavGraph(
    navController: NavHostController,
    postViewModel: PostViewModel
) {
    composable(POST) { PostScreen(navController = navController) }

    composable("$COMMUNITY_DETAIL_SCREEN/{postId}") { backStackEntry ->
        val postId = backStackEntry.arguments?.getString("postId") ?: return@composable
        CommunityDetailScreen(navController, postViewModel,postId)
    }

    composable(COMMUNITY_WRITE){ CommunityWrite(navController) }
}