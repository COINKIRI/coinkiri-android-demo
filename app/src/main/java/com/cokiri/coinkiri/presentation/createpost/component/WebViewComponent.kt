package com.cokiri.coinkiri.presentation.createpost.component

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.cokiri.coinkiri.presentation.createpost.CreatePostViewModel
import com.cokiri.coinkiri.presentation.main.MainActivity
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground

/**
 * WebView 컴포넌트
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComponent(
    communityWriteViewModel: CreatePostViewModel,
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
private fun WebView.setupWebView(viewModel: CreatePostViewModel) {
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