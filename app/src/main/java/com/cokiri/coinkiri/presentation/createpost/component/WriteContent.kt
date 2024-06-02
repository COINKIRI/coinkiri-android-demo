package com.cokiri.coinkiri.presentation.createpost.component

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.cokiri.coinkiri.presentation.createpost.CreatePostViewModel
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground

/**
 * 글 작성 화면의 내용을 보여주는 컴포넌트입니다.
 */
@Composable
fun WriteContent(
    communityWriteViewModel: CreatePostViewModel,
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