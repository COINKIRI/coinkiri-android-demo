package com.cokiri.coinkiri.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.createpost.CreatePostScreenForCommunity
import com.cokiri.coinkiri.presentation.post.PostScreen
import com.cokiri.coinkiri.presentation.post.PostViewModel
import com.cokiri.coinkiri.presentation.post.community.CommunityDetailScreen
import com.cokiri.coinkiri.util.COMMUNITY_DETAIL_SCREEN
import com.cokiri.coinkiri.util.CREATE_POST_SCREEN_FOR_COMMUNITY
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

    composable(CREATE_POST_SCREEN_FOR_COMMUNITY){ CreatePostScreenForCommunity(navController) }
}