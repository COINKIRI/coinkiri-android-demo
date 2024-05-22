package com.cokiri.coinkiri.presentation.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.util.COMMUNITYDETAIL
import com.cokiri.coinkiri.util.FOLLOW

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    navController: NavHostController
) {

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("커뮤니티", "뉴스", "미션")
    var isMenuExpanded by remember { mutableStateOf(false) } // 메뉴가 확장되었는지 여부를 저장하는 변수

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TabRow(selectedTabIndex = selectedTabIndex) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(title) }
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
                }
            )
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                when (selectedTabIndex) {
                    0 -> CommunityList(navController = navController)
                    1 -> NewsList()
                    2 -> MissionList()
                }
            }
        },
        floatingActionButton = {
            FloatingActionMenu(isMenuExpanded) { isMenuExpanded = !isMenuExpanded }
        }
    )

    // 배경이 어두운 투명색으로 변하는 효과
    if (isMenuExpanded) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(onClick = { isMenuExpanded = false })
        )
    }
}

@Composable
fun FloatingActionMenu(
    isMenuExpanded: Boolean,
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
                onClick = { /*TODO*/ }
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
        modifier = Modifier.wrapContentSize(),               // Row의 크기를 컨텐츠에 맞게 설정
        horizontalArrangement = Arrangement.spacedBy(10.dp), // 아이콘 간의 간격 설정
        verticalAlignment = Alignment.CenterVertically       // 아이콘을 세로로 중앙 정렬
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
fun CommunityList(navController: NavHostController) {
    LazyColumn {
        items(30) {
            CommunityCard(onclick = {navController.navigate(COMMUNITYDETAIL)})
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