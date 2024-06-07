package com.cokiri.coinkiri.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity
import com.cokiri.coinkiri.presentation.login.LoginUiState
import com.cokiri.coinkiri.presentation.login.LoginViewModel
import com.cokiri.coinkiri.presentation.profile.component.MemberInfoCard
import com.cokiri.coinkiri.presentation.profile.component.ProfileBottomSheet
import com.cokiri.coinkiri.ui.theme.CoinkiriBlack
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.util.LOGIN
import com.cokiri.coinkiri.util.PROFILE_MODIFY_SCREEN
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navController: NavHostController,
) {

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()
    val isLoading by profileViewModel.isLoading.collectAsStateWithLifecycle()
    val memberInfo by profileViewModel.memberInfo.collectAsStateWithLifecycle()


    /**
     * 로그인 상태가 초기 상태일 때 로그인 화면으로 이동
     */
    LaunchedEffect(loginUiState) {
        if (loginUiState is LoginUiState.Initial) {
            navController.navigate(LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (showBottomSheet) {
        LaunchedEffect(Unit) {
            sheetState.expand()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CoinkiriWhite,
                    titleContentColor = CoinkiriBlack,
                ),
                actions = {
                    IconButton(
                        onClick = { showBottomSheet = true }
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = ""
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.profile),
                        fontSize = 18.sp,
                    )
                }
            )
        },
        content = {paddingValues ->
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else{
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = {
                        isRefreshing = true
                        profileViewModel.fetchMemberInfo()
                    }
                ) {
                    ProfileScreenContent(
                        paddingValues = paddingValues,
                        navController = navController,
                        memberInfo = memberInfo
                    )
                }
            }

            if (showBottomSheet) {
                ProfileBottomSheet(
                    sheetState = sheetState,
                    onDismissSheet = { coroutineScope.launch { showBottomSheet = false } },
                    onModifyClick = {
                        navController.navigate(PROFILE_MODIFY_SCREEN)
                        coroutineScope.launch { showBottomSheet = false }

                    },
                    onLogoutClick = {
                        loginViewModel.kakaoLogout()
                        coroutineScope.launch { showBottomSheet = false }
                    }
                )
            }
        }
    )
}




@Composable
fun ProfileScreenContent(
    navController: NavHostController,
    paddingValues: PaddingValues,
    memberInfo: MemberInfoEntity?,
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        item{
            MemberInfoCard(
                memberInfo = memberInfo,
                navController = navController
            )
            MemberPostCard()
        }
    }
}


/**
 * 맴버 작성글
 */
@Preview
@Composable
fun MemberPostCard() {

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("분석글", "게시글")

    Card(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(CoinkiriWhite),
        elevation = CardDefaults.cardElevation(5.dp)
    ){
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = CoinkiriWhite,
            contentColor = CoinkiriBlack,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = CoinkiriBlack
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) },
                )
            }
        }
        when (selectedTabIndex) {
            0 -> AnalysisMemberCard()
            1 -> PostMemberCard()
        }
    }
}


@Preview
@Composable
fun AnalysisMemberCard(){
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ){
        Text(text = "분석글", modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
fun PostMemberCard(){
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ){
        Text(text = "일반글", modifier = Modifier.padding(16.dp))
    }
}