package com.cokiri.coinkiri.presentation.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity
import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisResponseDto
import com.cokiri.coinkiri.data.remote.model.post.community.CommunityResponseDto
import com.cokiri.coinkiri.presentation.analysis.AnalysisViewModel
import com.cokiri.coinkiri.presentation.analysis.component.AnalysisListItemCard
import com.cokiri.coinkiri.presentation.login.LoginUiState
import com.cokiri.coinkiri.presentation.login.LoginViewModel
import com.cokiri.coinkiri.presentation.post.PostViewModel
import com.cokiri.coinkiri.presentation.post.community.CommunityCard
import com.cokiri.coinkiri.presentation.profile.component.MemberInfoCard
import com.cokiri.coinkiri.presentation.profile.component.ProfileBottomSheet
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriBlack
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.util.ANALYSIS_DETAIL_SCREEN
import com.cokiri.coinkiri.util.COMMUNITY_DETAIL_SCREEN
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
    postViewModel: PostViewModel = hiltViewModel(),
    analysisViewModel: AnalysisViewModel = hiltViewModel(),
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

    /**
     * 좋아요 커뮤니티 목록을 가져옴
     */
    LaunchedEffect(Unit) {
        postViewModel.fetchLikeCommunityList()
    }

    val likeList by postViewModel.likeList.collectAsStateWithLifecycle()

    /**
     *  좋아요 분석글 목록을 가져옴
     */
    LaunchedEffect(Unit) {
        analysisViewModel.fetchLikeAnalysisList()
    }

    val analysisLikeList by analysisViewModel.likeAnalysisList.collectAsStateWithLifecycle()
    Log.d("analysisLikeList", "analysisLikeList : $analysisLikeList" )


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
                        memberInfo = memberInfo,
                        likeList = likeList,
                        analysisLikeList = analysisLikeList,
                        analysisViewModel = analysisViewModel,
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
    likeList: List<CommunityResponseDto>,
    analysisLikeList: List<AnalysisResponseDto>,
    analysisViewModel: AnalysisViewModel,
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .background(CoinkiriBackground)
            .fillMaxSize()
    ) {
        item{
            MemberInfoCard(
                memberInfo = memberInfo,
                navController = navController
            )
            MemberPostContent(
                likeList = likeList,
                analysisLikeList = analysisLikeList,
                analysisViewModel = analysisViewModel,
                navController = navController
            )
        }
    }
}




@Composable
fun MemberPostContent(
    likeList: List<CommunityResponseDto>,
    analysisLikeList: List<AnalysisResponseDto>,
    analysisViewModel: AnalysisViewModel,
    navController: NavHostController
) {

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("포스팅", "좋아요")

    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(CoinkiriWhite),
        elevation = CardDefaults.cardElevation(5.dp)
    ){
        TabRow(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
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
            0 -> MemberPostingContent()
            1 -> MemberLikedContent(
                likeList,
                analysisLikeList,
                analysisViewModel,
                navController
            )
        }
    }
}


@Composable
fun MemberLikedContent(
    likeList: List<CommunityResponseDto>,
    analysisLikeList: List<AnalysisResponseDto>,
    analysisViewModel: AnalysisViewModel,
    navController: NavHostController
) {

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("분석글", "게시글")

    Log.d("MemberLikedContent", "likeList: $likeList")

    Column {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                Button(
                    onClick = { selectedTabIndex = index },
                    colors = ButtonDefaults.buttonColors(if (selectedTabIndex == index) CoinkiriPointGreen else Color.LightGray)
                ) {
                    Text(text = tab)
                }
            }
        }

        when (selectedTabIndex) {
            0 -> AnalysisMemberCard(
                analysisLikeList,
                analysisViewModel,
                navController
            )
            1 -> PostMemberCard(
                likeList,
                navController
            )
        }
    }
}




@Composable
fun MemberPostingContent(

){

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("분석글", "게시글")

    Column {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                Button(
                    onClick = { selectedTabIndex = index },
                    colors = ButtonDefaults.buttonColors(if (selectedTabIndex == index) CoinkiriPointGreen else Color.LightGray)
                ) {
                    Text(text = tab)
                }
            }
        }

        when (selectedTabIndex) {
//            0 -> AnalysisMemberCard(analysisLikeList, analysisViewModel)
//            1 -> PostMemberCard(likeList = emptyList())
        }
    }
}




@Composable
fun AnalysisMemberCard(
    analysisLikeList: List<AnalysisResponseDto>,
    analysisViewModel: AnalysisViewModel,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        if (analysisLikeList.isEmpty()) {
            Text(
                text = "좋아요한 분석글이 없습니다.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(analysisLikeList.size) { index ->
                    val analysis = analysisLikeList[index]
                    val postId = analysisLikeList[index].postResponseDto.id
                    AnalysisListItemCard(
                        analysisResponseDto = analysis,
                        analysisViewModel = analysisViewModel,
                        analysisCardClick = { navController.navigate("$ANALYSIS_DETAIL_SCREEN/$postId") }
                    )
                }
            }
        }
    }
}



@Composable
fun PostMemberCard(
    likeList: List<CommunityResponseDto>,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        if (likeList.isEmpty()) {
            Text(
                text = "좋아요한 게시글이 없습니다.",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(likeList.size) { index ->
                    val community = likeList[index]
                    val postId = likeList[index].postResponseDto.id
                    CommunityCard(
                        communityResponseDto = community,
                        onclick =  { navController.navigate("$COMMUNITY_DETAIL_SCREEN/$postId") },
                    )
                }
            }
        }
    }
}