package com.cokiri.coinkiri.presentation.createpost

import android.util.Log
import android.webkit.WebView
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.presentation.createpost.component.WebViewComponent
import com.cokiri.coinkiri.presentation.createpost.component.WriteContent
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import org.json.JSONException
import org.json.JSONObject

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CreatePostScreen(
//    navController: NavHostController,
//    createPostViewModel: CreatePostViewModel = hiltViewModel()
//) {
//    val context = LocalContext.current
//    val webView = remember { WebView(context) }
//    val isLoading by createPostViewModel.isLoading.collectAsState()
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("글 작성") },
//                navigationIcon = {
//                    TextButton(onClick = { navController.popBackStack() }) {
//                        Text(text = "취소")
//                    }
//                },
//                actions = {
//                    TextButton(onClick = {
//                        handleContentSubmission(
//                            webView,
//                            createPostViewModel,
//                            navController
//                        )
//                    }) {
//                        Text(text = "등록")
//                    }
//                }
//            )
//        },
//        content = {
//            if (isLoading) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(CoinkiriBackground),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//            } else {
//                Column(
//                    modifier = Modifier
//                        .padding(it)
//                        .background(CoinkiriBackground)
//                ) {
//                    WriteContent(createPostViewModel, webView)
//                }
//            }
//        }
//    )
//}
//
//
//
//
//private fun handleContentSubmission(
//    webView: WebView,
//    createPostViewModel: CreatePostViewModel,
//    navController: NavHostController
//) {
//    webView.evaluateJavascript("sendContent()") { result ->
//        val content = result?.removeSurrounding("\"")
//        if (content != null) {
//            try {
//                val jsonObject = JSONObject(content)
//                val bodyContent = jsonObject.getString("content")
//                val images = jsonObject.getJSONArray("images")
//
//                // 본문 내용 업데이트
//                createPostViewModel.onContentChange(bodyContent)
//
//                // 이미지 업데이트
//                val imageList = mutableListOf<Pair<Int, String>>()
//                for (i in 0 until images.length()) {
//                    val imageObject = images.getJSONObject(i)
//                    val position = imageObject.getInt("position")
//                    val base64 = imageObject.getString("base64")
//                    imageList.add(Pair(position, base64))
//                }
//                createPostViewModel.onImagesChange(imageList)
//
//                // 포스트 제출
//                createPostViewModel.submitPostContent()
//                navController.popBackStack()
//            } catch (e: JSONException) {
//                e.printStackTrace()
//                Log.e("handleContentSubmission", "JSON parsing error: ${e.message}")
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Log.e("handleContentSubmission", "Error: ${e.message}")
//            }
//        }
//    }
//}
//


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostCommonScreen(
    title: String,
    onCancelClick: () -> Unit,
    onSubmitClick: () -> Unit,
    isLoading: Boolean,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    TextButton(onClick = onCancelClick) {
                        Text(text = "취소")
                    }
                },
                actions = {
                    TextButton(onClick = onSubmitClick) {
                        Text(text = "등록")
                    }
                }
            )
        },
        content = {
            if (isLoading) {
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
                    content()
                }
            }
        }
    )
}

