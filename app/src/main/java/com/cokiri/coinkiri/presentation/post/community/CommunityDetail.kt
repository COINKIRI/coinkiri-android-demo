package com.cokiri.coinkiri.presentation.post.community

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.presentation.post.PostViewModel
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.util.buildHtmlContent
import com.cokiri.coinkiri.util.byteArrayToString
import com.cokiri.coinkiri.util.insertImagesIntoContent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetail(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel(),
    postId: String
) {

    var webViewInstance: WebView? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    LaunchedEffect(postId) {
        postViewModel.fetchCommunityPostDetail(postId.toLong())
    }

    val communityDetail by postViewModel.communityDetail.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("커뮤니티") },
                navigationIcon = {
                    IconButton(onClick = {
                        webViewInstance?.let { webView ->
                            (webView.parent as? ViewGroup)?.removeView(webView)
                            Handler(Looper.getMainLooper()).post {
                                webView.clearCache(true)
                                webView.destroy()
                            }
                            webViewInstance = null
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        },
        content = { padding ->
            if (communityDetail != null) {
                val detail = communityDetail
                val content = detail?.postDetailResponseDto?.content
                val imagesList = detail?.postDetailResponseDto?.images
                val pairImagesList = imagesList?.map {
                    it.position to byteArrayToString(it.base64)
                }
                val newContent = insertImagesIntoContent(content, pairImagesList)

                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(CoinkiriBackground)
                ) {
                    item {
                        if (detail != null) {
                            TitleSection(title = detail.postDetailResponseDto.title)
                        }
                        ContentSection(newContent, context) { webView ->
                            webViewInstance = webView
                        }
                        CommentSection()
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CoinkiriBackground),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    )
}



@Composable
fun TitleSection(title: String) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .background(CoinkiriBackground)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoinkiriBackground)
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(5.dp),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoinkiriBackground)
                .padding(5.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Profile Image",
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(text = "레벨 + 작성자", fontWeight = FontWeight.Bold)
                    Text(text = "작성일")
                }
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(CoinkiriPointGreen),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(10.dp)
            ) {
                Text("팔로우")
            }
        }
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ContentSection(newContent: String, context: Context, onWebViewReady: (WebView) -> Unit) {
    AndroidView(
        factory = {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadDataWithBaseURL(
                    null,
                    buildHtmlContent(newContent),
                    "text/html",
                    "UTF-8",
                    null
                )
                onWebViewReady(this)
            }
        },
        update = { webView ->
            onWebViewReady(webView)
        },
        onRelease = { webView ->
            (webView.parent as? ViewGroup)?.removeView(webView)
            Handler(Looper.getMainLooper()).post {
                webView.clearCache(true)
                webView.destroy()
            }
        }
    )
}



@Preview
@Composable
fun CommentSection() {
    Column(
        modifier = Modifier
            .background(CoinkiriBackground)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(CoinkiriBackground)
                .padding(15.dp)
        ) {
            Text(text = "댓글(2)")
        }
        CommentCard()
        CommentCard()
    }
}

@Preview
@Composable
fun CommentCard(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp),
        colors = CardDefaults.cardColors(CoinkiriBackground)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "레벨 + 작성자", fontWeight = FontWeight.Bold)
                Text(text = "작성일")
            }
            Spacer(modifier = Modifier.size(10.dp))
            Text(text = "댓글 내용")
            Spacer(modifier = Modifier.size(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "2분전")
                Text(text = "작성일")
            }
        }
        HorizontalDivider()
    }
}