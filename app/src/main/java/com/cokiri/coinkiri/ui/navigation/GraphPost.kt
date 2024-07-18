package com.cokiri.coinkiri.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.cokiri.coinkiri.presentation.screens.createpost.CreatePostScreenForCommunity
import com.cokiri.coinkiri.presentation.screens.post.PostScreen
import com.cokiri.coinkiri.presentation.screens.post.PostViewModel
import com.cokiri.coinkiri.presentation.screens.post.community.CommunityDetailScreen
import com.cokiri.coinkiri.presentation.screens.post.news.NewsDetailScreen
import com.cokiri.coinkiri.util.COMMUNITY_DETAIL_SCREEN
import com.cokiri.coinkiri.util.CREATE_POST_SCREEN_FOR_COMMUNITY
import com.cokiri.coinkiri.util.NEWS_DETAIL_SCREEN
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

    composable("$NEWS_DETAIL_SCREEN/{newsLink}") { backStackEntry ->
        val newsLink = backStackEntry.arguments?.getString("newsLink") ?: return@composable
        NewsDetailScreen(navController,postViewModel, newsLink)
    }

    composable(CREATE_POST_SCREEN_FOR_COMMUNITY){ CreatePostScreenForCommunity(navController) }
}