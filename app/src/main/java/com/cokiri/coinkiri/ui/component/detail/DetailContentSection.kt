package com.cokiri.coinkiri.ui.component.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.cokiri.coinkiri.data.remote.model.PostDetailResponseDto
import com.cokiri.coinkiri.util.buildHtmlContent
import com.cokiri.coinkiri.util.byteArrayToString
import com.cokiri.coinkiri.util.insertImagesIntoContent

/**
 * 게시글(분석,커뮤니티) 상세화면의 내용 섹션
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DetailContentSection(
    postDetailResponseDto: PostDetailResponseDto,
    context: Context,
    onWebViewReady: (WebView) -> Unit
) {

    val content = postDetailResponseDto.content
    val imagesList = postDetailResponseDto.images
    val pairImagesList = imagesList.map {
        it.position to byteArrayToString(it.base64)
    }
    val newContent = insertImagesIntoContent(content, pairImagesList)

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
            webView.clearCache(true)
            webView.destroy()
        }
    )
}