package com.cokiri.coinkiri.presentation.post.community

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.PostDetailResponseDto
import com.cokiri.coinkiri.presentation.comment.CommentScreen
import com.cokiri.coinkiri.presentation.post.PostViewModel
import com.cokiri.coinkiri.ui.component.detail.DetailBottomAppBar
import com.cokiri.coinkiri.ui.component.detail.DetailContentSection
import com.cokiri.coinkiri.ui.component.detail.DetailTitleSection
import com.cokiri.coinkiri.ui.component.detail.DetailTopAppBar
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.util.buildHtmlContent
import com.cokiri.coinkiri.util.byteArrayToPainter
import com.cokiri.coinkiri.util.byteArrayToString
import com.cokiri.coinkiri.util.insertImagesIntoContent
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

    val communityDetail by postViewModel.communityDetail.collectAsStateWithLifecycle()

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
                communityDetail = communityDetail,
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
    communityDetail: CommunityDetailResponseDto?,
    context: Context,
    webView: (WebView) -> Unit
) {
    when {
        communityDetail != null -> {
            val postDetailResponseDto = communityDetail.postDetailResponseDto
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

