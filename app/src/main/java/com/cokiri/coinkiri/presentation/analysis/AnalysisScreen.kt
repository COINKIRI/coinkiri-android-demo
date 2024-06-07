package com.cokiri.coinkiri.presentation.analysis

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisResponseDto
import com.cokiri.coinkiri.presentation.analysis.component.AnalysisListItemCard
import com.cokiri.coinkiri.ui.component.FloatingActionMenu
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.util.ANALYSIS_DETAIL_SCREEN
import com.cokiri.coinkiri.util.ANALYSIS_WRITE_SCREEN
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun AnalysisScreen(
    navController: NavHostController,
    analysisViewModel: AnalysisViewModel = hiltViewModel()
) {
    val menuItems = listOf(
        Triple("분석글 작성", Icons.Default.Create) { navController.navigate(ANALYSIS_WRITE_SCREEN) }
    )
    var isMenuExpanded by remember { mutableStateOf(false) }

    val isLoading by analysisViewModel.isLoading.collectAsState()
    val errorMessage by analysisViewModel.errorMessage.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    val analysisPostList by analysisViewModel.analysisPostList.collectAsState()

    Scaffold(
        topBar = { AnalysisTopBar() },
        floatingActionButton = {
            FloatingActionMenu(
                isMenuExpanded = isMenuExpanded,
                onMenuToggle = { isMenuExpanded = !isMenuExpanded },
                menuItems = menuItems
            )
        },
        content = { paddingValues ->
            AnalysisSwipeRefreshContent(
                isLoading = isLoading,
                errorMessage = errorMessage,
                analysisPostList = analysisPostList,
                isRefreshing = isRefreshing,
                swipeRefreshState = swipeRefreshState,
                onRefresh = {
                    isRefreshing = true
                    analysisViewModel.fetchAllAnalysisPostList()
                },
                paddingValues = paddingValues,
                navController = navController,
                analysisViewModel = analysisViewModel
            )
        }
    )

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            isRefreshing = false
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisTopBar() {
    TopAppBar(
        title = { Text("분석") },
        colors = TopAppBarDefaults.topAppBarColors(CoinkiriWhite),
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
}



@Composable
fun AnalysisSwipeRefreshContent(
    isLoading: Boolean,
    errorMessage: String?,
    analysisPostList: List<AnalysisResponseDto>,
    isRefreshing: Boolean,
    swipeRefreshState: SwipeRefreshState,
    onRefresh: () -> Unit,
    paddingValues: PaddingValues,
    navController: NavHostController,
    analysisViewModel: AnalysisViewModel
) {
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = errorMessage)
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .background(CoinkiriBackground)
                    ) {
                        items(analysisPostList.size) { index ->
                            val analysisPost = analysisPostList[index]
                            val postId = analysisPost.postResponseDto.id
                            AnalysisListItemCard(
                                analysisResponseDto = analysisPost,
                                analysisViewModel = analysisViewModel,
                                analysisCardClick = { navController.navigate("$ANALYSIS_DETAIL_SCREEN/$postId") }
                            )
                        }
                    }
                }
            }
        }
    }
}
