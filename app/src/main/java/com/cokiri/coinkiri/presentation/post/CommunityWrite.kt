package com.cokiri.coinkiri.presentation.post

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cokiri.coinkiri.presentation.main.MainActivity
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityWrite(
    navController: NavHostController,
    communityWriteViewModel: CommunityWriteViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val webView = remember { WebView(context) }


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
                        // JavaScript 함수 호출하여 에디터 내용 가져오기
                        webView.evaluateJavascript("sendEditorContentToAndroid();", null)
                        // 뷰모델의 submitPost 함수 호출
                        communityWriteViewModel.submitPost()
                    }) {
                        Text(text = "등록")
                    }
                }
            )
        },
        content = {
            Column(modifier = Modifier
                .padding(it)
                .background(CoinkiriBackground)) {
                WriteContent(communityWriteViewModel)
            }
        },
        bottomBar = {

        }
    )
}


@Composable
fun WriteContent(communityWriteViewModel: CommunityWriteViewModel) {

    val title by communityWriteViewModel.title.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoinkiriBackground)
    ) {
        // 제목입력
        TextField(
            value = title,
            onValueChange = { newTitle -> communityWriteViewModel.onTitleChange(newTitle) },
            placeholder = { Text("제목을 입력해주세요.") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = CoinkiriBackground,
                unfocusedContainerColor = CoinkiriBackground,
            ),
            modifier = Modifier.fillMaxWidth()
        )
        WebViewComponent(communityWriteViewModel)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComponent(communityWriteViewModel: CommunityWriteViewModel) {
    val context = LocalContext.current
    val webView = remember { WebView(context) }

    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(CoinkiriBackground),
        factory = {
            webView.apply {
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
                loadUrl("file:///android_asset/editor.html")
                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun getEditorContent(content: String) {
                        communityWriteViewModel.onContentChange(content)
                    }
                }, "Android")
            }
        },
        update = { webView ->
            webView.settings.javaScriptEnabled = true
            webView.loadUrl("file:///android_asset/editor.html")
        }
    )
}

@Preview
@Composable
fun CommunityWritePreview() {
    val navController = rememberNavController() // 임시 NavController 생성
    CommunityWrite(navController = navController)
}

@Preview
@Composable
fun WriteContentPreview() {
    WriteContent(
        communityWriteViewModel = CommunityWriteViewModel()
    )
}