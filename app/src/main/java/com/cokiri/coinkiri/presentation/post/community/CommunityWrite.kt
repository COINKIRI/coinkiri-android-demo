package com.cokiri.coinkiri.presentation.post.community

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.presentation.main.MainActivity
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import org.json.JSONException
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityWrite(
    navController: NavHostController,
    communityWriteViewModel: CommunityWriteViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val webView = remember { WebView(context) }
    val isLoading by communityWriteViewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("커뮤니티 글 작성") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text(text = "취소")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        handleContentSubmission(
                            webView,
                            communityWriteViewModel,
                            navController
                        )
                    }) {
                        Text(text = "등록")
                    }
                }
            )
        },
        content = {
            if (isLoading) {
                // 로딩 중일 때 로딩 인디케이터 표시
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CoinkiriBackground),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .background(CoinkiriBackground)
                ) {
                    WriteContent(communityWriteViewModel, webView)
                }
            }
        }
    )
}

@Composable
fun WriteContent(
    communityWriteViewModel: CommunityWriteViewModel,
    webView: WebView
) {
    val title by communityWriteViewModel.title.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoinkiriBackground)
    ) {
        TextField(
            value = title,
            onValueChange = { newTitle -> communityWriteViewModel.onTitleChange(newTitle) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("제목을 입력해주세요.") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = CoinkiriBackground,
                unfocusedContainerColor = CoinkiriBackground,
            )
        )
        WebViewComponent(communityWriteViewModel, webView)
    }
}



/**
 * WebView 컴포넌트
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComponent(
    communityWriteViewModel: CommunityWriteViewModel,
    webView: WebView
) {
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(CoinkiriBackground),
        factory = {
            webView.apply {
                setupWebView(communityWriteViewModel)
            }
        },
        update = {
            it.loadUrl("file:///android_asset/editor.html")
        }
    )
}


/**
 * WebView 설정(JavaScript 활성화, 파일 업로드 처리)
 */
@SuppressLint("SetJavaScriptEnabled")
private fun WebView.setupWebView(viewModel: CommunityWriteViewModel) {
    settings.javaScriptEnabled = true
    webViewClient = WebViewClient()
    webChromeClient = object : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            (context as MainActivity).openFileChooser(filePathCallback)
            return true
        }
    }
    addJavascriptInterface(JavaScriptInterface(viewModel), "AndroidInterface")
    loadUrl("file:///android_asset/editor.html")
}


private fun handleContentSubmission(
    webView: WebView,
    viewModel: CommunityWriteViewModel,
    navController: NavHostController
) {
    webView.evaluateJavascript("sendContent()") { result ->
        val content = result?.removeSurrounding("\"")
        if (content != null) {
            try {
                val jsonObject = JSONObject(content)
                val bodyContent = jsonObject.getString("content")
                val images = jsonObject.getJSONArray("images")

                // 본문 내용 업데이트
                viewModel.onContentChange(bodyContent)

                // 이미지 업데이트
                val imageList = mutableListOf<Pair<Int, String>>()
                for (i in 0 until images.length()) {
                    val imageObject = images.getJSONObject(i)
                    val position = imageObject.getInt("position")
                    val base64 = imageObject.getString("base64")
                    imageList.add(Pair(position, base64))
                }
                viewModel.onImagesChange(imageList)

                // 포스트 제출
                viewModel.submitPost()
                navController.popBackStack()
            } catch (e: JSONException) {
                e.printStackTrace()
                Log.e("handleContentSubmission", "JSON parsing error: ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("handleContentSubmission", "Error: ${e.message}")
            }
        }
    }
}


/**
 * WebView에서 JavaScript와의 상호작용을 위한 인터페이스
 * JavaScript에서 AndroidInterface.receiveContent(content)로 호출할 수 있다.
 */
class JavaScriptInterface(
    private val viewModel: CommunityWriteViewModel
) {
    @JavascriptInterface
    fun receiveContent(content: String) {
        handleReceivedContent(content, viewModel)
    }
}


private fun handleReceivedContent(
    content: String,
    viewModel: CommunityWriteViewModel
) {
    try {
        val jsonObject = JSONObject(content)
        val bodyContent = jsonObject.getString("content")
        val images = jsonObject.getJSONArray("images")

        // 본문 내용 업데이트
        viewModel.onContentChange(bodyContent)

        // 이미지 업데이트
        val imageList = mutableListOf<Pair<Int, String>>()
        for (i in 0 until images.length()) {
            val imageObject = images.getJSONObject(i)
            val position = imageObject.getInt("position")
            val base64 = imageObject.getString("base64")
            imageList.add(Pair(position, base64))
        }
        viewModel.onImagesChange(imageList)
    } catch (e: JSONException) {
        e.printStackTrace()
        Log.e("handleReceivedContent", "JSON parsing error: ${e.message}")
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("handleReceivedContent", "Error: ${e.message}")
    }
}