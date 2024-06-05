package com.cokiri.coinkiri.presentation.post.community

import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.presentation.comment.CommentScreen
import com.cokiri.coinkiri.presentation.post.PostViewModel
import com.cokiri.coinkiri.ui.component.detail.DetailAuthorProfile
import com.cokiri.coinkiri.ui.component.detail.DetailBottomAppBar
import com.cokiri.coinkiri.ui.component.detail.DetailContentSection
import com.cokiri.coinkiri.ui.component.detail.DetailTitleSection
import com.cokiri.coinkiri.ui.component.detail.DetailTopAppBar
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetailScreen(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel(),
    stringPostId: String
) {

    val postId = stringPostId.toLong()
    var webViewInstance: WebView? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    LaunchedEffect(postId) {
        postViewModel.fetchCommunityPostDetails(postId)
    }

    DisposableEffect(Unit) {
        onDispose {
            webViewInstance?.let { webView ->
                (webView.parent as? ViewGroup)?.removeView(webView)
                webView.clearCache(true)
                webView.destroy()
                webViewInstance = null
            }
        }
    }

    val communityDetailResponseDto by postViewModel.communityDetail.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        LaunchedEffect(Unit) {
            sheetState.expand()
        }
    }

    Scaffold(
        topBar = {
            DetailTopAppBar(
                backClick = {
                    webViewInstance?.let { webView ->
                        (webView.parent as? ViewGroup)?.removeView(webView)
                        webView.clearCache(true)
                        webView.destroy()
                        webViewInstance = null
                    }
                    navController.popBackStack()
                }
            )
        },
        content = { paddingValues ->
            CommunityContent(
                paddingValues = paddingValues,
                communityDetailResponseDto = communityDetailResponseDto,
                context = context,
                webView = { webView ->
                    webViewInstance = webView
                }
            )

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { coroutineScope.launch { showBottomSheet = false } },
                    sheetState = sheetState,
                    containerColor = CoinkiriWhite,
                ) {

                    /**
                     * 댓글 작성화면
                     */
                    CommentScreen(
                        closeClick = { coroutineScope.launch { showBottomSheet = false } },
                        postId = postId
                    )

                }
            }
        },
        bottomBar = {
            DetailBottomAppBar(
                clickComment = { coroutineScope.launch { showBottomSheet = true } }
            )
        }
    )
}


/**
 * 커뮤니티 작성글 화면의 content
 */
@Composable
fun CommunityContent(
    paddingValues: PaddingValues,
    communityDetailResponseDto: CommunityDetailResponseDto?,
    context: Context,
    webView: (WebView) -> Unit
) {
    when {
        communityDetailResponseDto != null -> {
            val postDetailResponseDto = communityDetailResponseDto.postDetailResponseDto
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(vertical = 10.dp)
                    .fillMaxSize()
                    .background(CoinkiriWhite)
            ) {
                item {
                    DetailTitleSection(postDetailResponseDto = postDetailResponseDto)
                    DetailContentSection(postDetailResponseDto, context) {
                        webView(it)
                    }
                    DetailAuthorProfile()
                }
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CoinkiriWhite),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

