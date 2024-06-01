package com.cokiri.coinkiri.presentation.post

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.presentation.post.community.CommunityList
import com.cokiri.coinkiri.ui.component.FloatingActionMenu
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.util.CREATE_POST_SCREEN

@SuppressLint("RememberReturnType")
@Composable
fun PostScreen(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel()
) {
    val tabs = remember { listOf("커뮤니티", "뉴스", "미션") }
    val menuItems = listOf(
        Triple("커뮤니티 글작성", Icons.Default.Create) { navController.navigate(CREATE_POST_SCREEN) },
        Triple("미션 생성", Icons.Default.Create) { /*TODO*/ }
    )

    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // 초기 화면 로드 시 게시글 리스트 불러오기
    LaunchedEffect(Unit) {
        postViewModel.fetchAllCommunityPostList()
    }

    Scaffold(
        topBar = {
            PostScreenTopBar(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                tabs = tabs
            )
        },
        content = { paddingValues ->
            PostScreenContent(
                selectedTabIndex = selectedTabIndex,
                navController = navController,
                postViewModel = postViewModel,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(CoinkiriBackground)
            )
        },
        floatingActionButton = {
            FloatingActionMenu(
                isMenuExpanded = isMenuExpanded,
                onMenuToggle = { isMenuExpanded = !isMenuExpanded },
                menuItems = menuItems
            )
        }
    )
}


/**
 * 게시글 목록 화면의 상단바
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreenTopBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String>
) {
    TopAppBar(
        title = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = CoinkiriBackground,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = CoinkiriPointGreen
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { onTabSelected(index) },
                        selectedContentColor = CoinkiriPointGreen,
                        text = {
                            Text(
                                title,
                                fontSize = 15.sp
                            )
                        }
                    )
                }
            }

        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(CoinkiriBackground)
    )
}


/**
 * 게시글 목록 화면의 내용
 */
@Composable
fun PostScreenContent(
    selectedTabIndex: Int,
    navController: NavHostController,
    postViewModel: PostViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        when (selectedTabIndex) {
            0 -> CommunityList(navController, postViewModel)
            1 -> NewsList()
            2 -> MissionList()
        }
    }
}



@Composable
fun NewsList() {
    Text(text = "NewsList")
}

@Composable
fun MissionList() {
    Text(text = "MissionList")
}
