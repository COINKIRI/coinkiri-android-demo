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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.util.byteArrayToString

@SuppressLint("RememberReturnType", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetail(
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel(),
    postId: String
) {

    LaunchedEffect(postId) {
        postViewModel.fetchCommunityPostDetail(postId.toLong())
    }

    val communityDetail by postViewModel.communityDetail.collectAsState()

    val title = communityDetail?.postDetailResponseDto?.title

    val content = communityDetail?.postDetailResponseDto?.content
    val imagesList = communityDetail?.postDetailResponseDto?.images
    val pairImagesList = imagesList?.map {
        it.position to byteArrayToString(it.base64)
    }

    // HTML 태그를 특수 문자로 대체하는 함수
    fun replaceHtmlTagsWithPlaceholders(content: String): Pair<String, Map<Int, String>> {
        val tagPattern = "<[^>]+>".toRegex()
        var placeholderContent = content
        val tagMap = mutableMapOf<Int, String>()
        var offset = 0
        tagPattern.findAll(content).forEach { matchResult ->
            val tag = matchResult.value
            val startIndex = matchResult.range.first + offset
            placeholderContent = placeholderContent.replaceRange(startIndex, startIndex + tag.length, "\u0001")
            tagMap[startIndex] = tag
            offset -= (tag.length - 1)  // Adjust offset due to replacement
        }
        return placeholderContent to tagMap
    }

    // 특수 문자를 HTML 태그로 복원하는 함수
    fun restoreHtmlTagsFromPlaceholders(content: String, tagMap: Map<Int, String>): String {
        var restoredContent = content
        tagMap.toSortedMap(compareByDescending { it }).forEach { (index, tag) ->
            restoredContent = restoredContent.replaceRange(index, index + 1, tag)
        }
        return restoredContent
    }

    // 이미지 삽입 함수를 정의합니다.
    fun mergeContentAndImages(content: String, pairImagesList: List<Pair<Int, String>>): String {
        val (placeholderContent, tagMap) = replaceHtmlTagsWithPlaceholders(content)
        var mutableContent = placeholderContent
        var cumulativeAdjustment = 0

        pairImagesList.forEach { (position, imageTag) ->
            val adjustedPosition = position + cumulativeAdjustment
            if (adjustedPosition <= mutableContent.length) {
                mutableContent = mutableContent.substring(0, adjustedPosition) + imageTag + mutableContent.substring(adjustedPosition)
                cumulativeAdjustment += imageTag.length
            } else {
                mutableContent += imageTag
            }
        }

        return restoreHtmlTagsFromPlaceholders(mutableContent, tagMap)
    }

    val combinedContent = content?.let { mergeContentAndImages(it, pairImagesList ?: emptyList()) }

    val MAX_LOG_LENGTH = 4000

    fun logLongMessage(tag: String, message: String) {
        if (message.length > MAX_LOG_LENGTH) {
            val chunkCount = message.length / MAX_LOG_LENGTH
            for (i in 0..chunkCount) {
                val max = MAX_LOG_LENGTH * (i + 1)
                if (max >= message.length) {
                    Log.d(tag, message.substring(MAX_LOG_LENGTH * i))
                } else {
                    Log.d(tag, message.substring(MAX_LOG_LENGTH * i, max))
                }
            }
        } else {
            Log.d(tag, message)
        }
    }
    logLongMessage("combinedContent", combinedContent ?: "N/A")
//    Log.d("combinedContent", "combinedContent: $combinedContent")

    Log.d("imagesList", "content.length: ${content?.length}")

    Log.d("content", "content: $content")
    Log.d("pairImagesList", "pairImagesList: $pairImagesList")

    // 결과 출력
    //val newContent = contentWithImages.toString()
    //Log.d("newContent", "newContent: $newContent")
    Log.d("CommunityDetail", "pairImagesList: $pairImagesList")


    val base64images = communityDetail?.postDetailResponseDto?.images?.map { it.base64 }
    val images = byteArrayToString(base64images?.get(index = 0) ?: "")
    val newContent = content + images


    Scaffold(
            topBar = {
                TopAppBar(

                    title = {
                        Text(text = title ?: "N/A")
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
                    TitleSection(title = title ?: "N/A")
                    ContentSection(combinedContent ?: "N/A")
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
    fun ContentSection(contentAndImages: String) {
        val context = LocalContext.current

        AndroidView(
            factory = {
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    loadDataWithBaseURL(
                        null,
                        buildHtmlContent(contentAndImages),
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            }, update = { webView ->
                webView.loadDataWithBaseURL(
                    null,
                    buildHtmlContent(contentAndImages),
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        )
    }


    fun buildHtmlContent(contentAndImages: String): String {
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
            <div id="editor">$contentAndImages</div>
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
