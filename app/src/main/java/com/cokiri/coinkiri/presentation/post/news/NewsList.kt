package com.cokiri.coinkiri.presentation.post.news

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.presentation.post.PostViewModel
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.util.NEWS_DETAIL_SCREEN
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun NewsList(
    navController: NavHostController,
    postViewModel: PostViewModel
) {

    // 초기 화면 로드 시 게시글 리스트 불러오기
    LaunchedEffect(Unit) {
        postViewModel.fetchAllNewsList()
    }

    val newsList by postViewModel.newsList.collectAsState()

    val isLoading by postViewModel.isLoading.collectAsState()
    val errorMessage by postViewModel.errorMessage.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            isRefreshing = true
            postViewModel.fetchAllCommunityPostList()
            isRefreshing = false
        },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                contentColor = CoinkiriPointGreen
            )
        }
    ) {
        if (isLoading) {
            // 로딩 상태일 때 로딩 인디케이터 표시
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            // 에러 상태일 때 에러 메시지 표시
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage ?: "Unknown error")
            }
        } else {
            LazyColumn(
                modifier = Modifier.background(CoinkiriBackground)
            ) {
                items(newsList.size) { index ->
                    val newsList = newsList[index]
                    val newsLink = newsList.link
                    NewsCard(
                        newsList = newsList,
                        newsCardClick = {
                            navController.navigate("$NEWS_DETAIL_SCREEN/${Uri.encode(newsLink)}")
                        }
                    )
                }
            }
        }
    }
}