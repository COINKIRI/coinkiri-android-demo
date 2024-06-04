package com.cokiri.coinkiri.presentation.analysis

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.presentation.analysis.component.AnalysisListItemCard
import com.cokiri.coinkiri.ui.component.FloatingActionMenu
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.util.ANALYSIS_WRITE_SCREEN
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    navController: NavHostController,
    analysisViewModel: AnalysisViewModel = hiltViewModel()
) {
    val menuItems = listOf(Triple("분석글 작성", Icons.Default.Create) { navController.navigate(ANALYSIS_WRITE_SCREEN)})
    var isMenuExpanded by remember { mutableStateOf(false) }

    val isLoading by analysisViewModel.isLoading.collectAsState()
    val errorMessage by analysisViewModel.errorMessage.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    val analysisPostList by analysisViewModel.analysisPostList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("분석") },
                colors = TopAppBarDefaults.topAppBarColors(CoinkiriBackground),
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(
                            painter = painterResource(R.drawable.ic_navi_home),
                            contentDescription = "",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionMenu(
                isMenuExpanded = isMenuExpanded,
                onMenuToggle = { isMenuExpanded = !isMenuExpanded },
                menuItems = menuItems
            )
        },
        content = { paddingValues ->
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    isRefreshing = true
                    analysisViewModel.fetchAllAnalysisPostList()
                }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when {
                        isLoading -> {
                            // 로딩 상태일 때 로딩 인디케이터 표시
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        errorMessage != null -> {
                            // 에러 상태일 때 에러 메시지 표시
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = errorMessage ?: "Unknown error")
                            }
                        }
                        else -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                            ) {
                                items(analysisPostList.size) { index ->
                                    val analysisPost = analysisPostList[index]
                                    AnalysisListItemCard(
                                        analysisResponseDto = analysisPost,
                                        analysisViewModel = analysisViewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )

    // Refresh 완료 시 상태 업데이트
    LaunchedEffect(isLoading) {
        if (!isLoading) {
            isRefreshing = false
        }
    }
}