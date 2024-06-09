package com.cokiri.coinkiri.presentation.createpost

import android.util.Log
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.presentation.createpost.component.WriteContent
import com.cokiri.coinkiri.util.POST
import org.json.JSONException
import org.json.JSONObject


@Composable
fun CreatePostScreenForCommunity(
    navController: NavHostController,
    createPostViewModel: CreatePostViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val webView = remember { WebView(context) }
    val isLoading by createPostViewModel.isLoading.collectAsState()

    CreatePostCommonScreen(
        title = "커뮤니티 글 작성",
        onCancelClick = { navController.popBackStack() },
        onSubmitClick = {
            handleContentSubmissionWithoutOptions(
                webView,
                createPostViewModel,
                navController
            )
        },
        isLoading = isLoading
    ) {
        WriteContent(createPostViewModel, webView)
    }
}

private fun handleContentSubmissionWithoutOptions(
    webView: WebView,
    viewModel: CreatePostViewModel,
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
                viewModel.submitPostContent()
                navController.navigate(POST)
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
