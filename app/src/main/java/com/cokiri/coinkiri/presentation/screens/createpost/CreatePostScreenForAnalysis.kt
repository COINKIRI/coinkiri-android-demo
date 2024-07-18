package com.cokiri.coinkiri.presentation.screens.createpost

import android.util.Log
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cokiri.coinkiri.presentation.screens.analysis.AnalysisViewModel
import com.cokiri.coinkiri.presentation.screens.createpost.component.WriteContent
import com.cokiri.coinkiri.util.ANALYSIS
import org.json.JSONException
import org.json.JSONObject

@Composable
fun CreatePostScreenForAnalysis(
    navController: NavHostController,
    createPostViewModel: CreatePostViewModel = hiltViewModel(),
    analysisViewModel: AnalysisViewModel = hiltViewModel()
) {

    val selectedCoinId by analysisViewModel.selectedCoinId.collectAsState()
    val selectedCoinPrevClosingPrice by analysisViewModel.selectedCoinPrevClosingPrice.collectAsState()
    val selectedInvestmentOption by analysisViewModel.selectedInvestmentOption.collectAsState()
    val selectedTargetPrice by analysisViewModel.selectedTargetPrice.collectAsState()
    val selectedTargetPeriod by analysisViewModel.selectedTargetPeriod.collectAsState()

    // 데이터 설정
    LaunchedEffect(Unit) {
        createPostViewModel.setAnalysisData(
            coinId = selectedCoinId,
            coinPrevClosingPrice = selectedCoinPrevClosingPrice,
            investmentOption = selectedInvestmentOption,
            targetPrice = selectedTargetPrice,
            targetPeriod = selectedTargetPeriod
        )
    }

    Log.d("CreatePostScreenForAnalysis", "selectedCoinId: $selectedCoinId")
    Log.d("CreatePostScreenForAnalysis", "selectedCoinPrevClosingPrice: $selectedCoinPrevClosingPrice")
    Log.d("CreatePostScreenForAnalysis", "selectedInvestmentOption: $selectedInvestmentOption")
    Log.d("CreatePostScreenForAnalysis", "selectedTargetPrice: $selectedTargetPrice")
    Log.d("CreatePostScreenForAnalysis", "selectedTargetPeriod: $selectedTargetPeriod")


    val context = LocalContext.current
    val webView = remember { WebView(context) }
    val isLoading by createPostViewModel.isLoading.collectAsState()

    CreatePostCommonScreen(
        title = "분석글 작성",
        onCancelClick = { navController.popBackStack() },
        onSubmitClick = {
            handleContentSubmission(
                webView,
                createPostViewModel,
                analysisViewModel,
                navController,
            )
        },
        isLoading = isLoading
    ) {
        WriteContent(createPostViewModel, webView)
    }
}

private fun handleContentSubmission(
    webView: WebView,
    createPostViewModel: CreatePostViewModel,
    analysisViewModel: AnalysisViewModel,
    navController: NavHostController,
) {
    webView.evaluateJavascript("sendContent()") { result ->
        val content = result?.removeSurrounding("\"")
        if (content != null) {
            try {
                val jsonObject = JSONObject(content)
                val bodyContent = jsonObject.getString("content")
                val images = jsonObject.getJSONArray("images")

                // 본문 내용 업데이트
                createPostViewModel.onContentChange(bodyContent)

                // 이미지 업데이트
                val imageList = mutableListOf<Pair<Int, String>>()
                for (i in 0 until images.length()) {
                    val imageObject = images.getJSONObject(i)
                    val position = imageObject.getInt("position")
                    val base64 = imageObject.getString("base64")
                    imageList.add(Pair(position, base64))
                }
                createPostViewModel.onImagesChange(imageList)


                // 포스트 제출
                createPostViewModel.submitAnalysisPostContent()
                analysisViewModel.resetState()
                navController.navigate(ANALYSIS)
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
