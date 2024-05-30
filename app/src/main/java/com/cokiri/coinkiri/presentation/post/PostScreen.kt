package com.cokiri.coinkiri.presentation.post

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.presentation.post.community.CommunityList
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.util.COMMUNITY_WRITE

@SuppressLint("RememberReturnType")
@Composable
fun PostScreen(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel()
) {
    val tabs = remember { listOf("커뮤니티", "뉴스", "미션") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var isMenuExpanded by remember { mutableStateOf(false) }

    // 초기 화면 로드 시 게시글 리스트 불러오기
    LaunchedEffect(Unit) {
        postViewModel.fetchCommunityPostList()
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
                isMenuExpanded,
                navController
            ) {
                isMenuExpanded = !isMenuExpanded
            }
        }
    )
}



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
fun FloatingActionMenu(
    isMenuExpanded: Boolean,
    navController: NavHostController,
    onMenuToggle: () -> Unit
) {
    if (!isMenuExpanded) {
        FloatingActionButton(
            onClick = onMenuToggle,
            modifier = Modifier.size(55.dp),
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add"
            )
        }
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.End,
        ) {
            FloatingActionButtonWithLabel(
                label = "커뮤니티 글작성",
                icon = Icons.Default.Create,
                onClick = { navController.navigate(COMMUNITY_WRITE) }
            )
            FloatingActionButtonWithLabel(
                label = "미션 생성",
                icon = Icons.Default.Create,
                onClick = { /*TODO*/ }
            )
            FloatingActionButton(
                onClick = onMenuToggle
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }
    }
}

@Composable
fun FloatingActionButtonWithLabel(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 10.sp
        )
        FloatingActionButton(
            onClick = onClick,
        ) {
            Icon(
                icon,
                contentDescription = label
            )
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
