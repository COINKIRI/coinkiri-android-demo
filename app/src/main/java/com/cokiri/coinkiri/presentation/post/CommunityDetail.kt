package com.cokiri.coinkiri.presentation.post

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen

@SuppressLint("RememberReturnType", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetail(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel(),
    postId: String
) {

    LaunchedEffect(postId) {
        if (postViewModel.communityPostList.value.isEmpty()) {
            postViewModel.fetchCommunityPostList()
        }
        postViewModel.fetchCommunityDetail(postId)
    }

    Log.d("CommunityDetail", "postId: $postId")
    val communityDetail by postViewModel.communityDetail.collectAsState()
    val title = communityDetail?.postResponseDto?.title
    val content = communityDetail?.postResponseDto?.content ?: ""
    val images = communityDetail?.postResponseDto?.images ?: emptyList()
    Log.d("CommunityDetail", "content: $content")
    Log.d("CommunityDetail", "images: $images")



    Scaffold(
        topBar = {
            TopAppBar(

                title = {
                    if (title != null) {
                        Text(title)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(CoinkiriBackground)
            ) {
                TitleSection()
                ContentSection(content)
            }
        }
    )
}

@Composable
fun TitleSection() {
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
                text = "여기 작성된글 제목",
                modifier = Modifier.padding(5.dp),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
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
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(
                        text = "레벨 + 작성자",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "작성일"
                    )
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

@SuppressLint("SetJavaScriptEnabled", "SuspiciousIndentation")
@Composable
fun ContentSection(content: String) {
    val context = LocalContext.current

        AndroidView(
            factory = {
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    loadDataWithBaseURL(
                        null,
                        buildHtmlContent(content),
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            }, update = { webView ->
                webView.loadDataWithBaseURL(
                    null,
                    buildHtmlContent(content),
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        )
    }



fun buildHtmlContent(content: String): String {
    val quillCssCdn = "https://cdn.jsdelivr.net/npm/quill@2.0.2/dist/quill.snow.css"
    val quillJsCdn = "https://cdn.jsdelivr.net/npm/quill@2.0.2/dist/quill.js"

    return """
        <!DOCTYPE html>
        <html>
        <head>
            <link href="$quillCssCdn" rel="stylesheet">
            <style>
                body {
                    background-color: #F8F8F8;
                    margin: 0;
                    padding: 0;
                    display: flex;
                    flex-direction: column;
                    height: 100vh;
                    box-sizing: border-box;
                }
                #editor-container {
                    flex: 1;
                }
                #editor {
                    width: 100%;
                    min-height: 500px;
                    background-color: #F8F8F8;
                }
            </style>
        </head>
        <body>
            <div id="editor">$content</div>
            <script src="$quillJsCdn"></script>
            <script>
                var quill = new Quill('#editor', {
                    theme: 'snow',
                    readOnly: true,
                    modules: {
                        toolbar: false
                    }
                });
            </script>
        </body>
        </html>
    """.trimIndent()
}


@Preview
@Composable
fun PreviewTitleSection() {
    TitleSection()
}


