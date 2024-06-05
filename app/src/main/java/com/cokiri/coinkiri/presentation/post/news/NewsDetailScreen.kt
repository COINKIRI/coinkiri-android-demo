package com.cokiri.coinkiri.presentation.post.news

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.R

@Composable
fun NewsDetailScreen(

    navController: NavHostController,
    newsLink: String
) {

    Scaffold (
        topBar = {
            NewsDetailTopBar(
                backClick = { navController.popBackStack() }
            )
        },
        content = {paddingValues ->
            NewsDetailContent(
                paddingValues,
                newsLink
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailTopBar(
    backClick: () -> Unit
) {
    TopAppBar(
        title = { /*TODO*/ },
        navigationIcon = {
            IconButton(
                onClick = backClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back Button"
                )
            }
        }
    )
}


@Composable
fun NewsDetailContent(
    paddingValues: PaddingValues,
    newsLink: String
) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            WebView(context).apply {
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        // 현재 페이지에서 벗어나지 않도록 설정
                        return true
                    }
                }
                loadUrl(newsLink)
            }
        },
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    )
}
