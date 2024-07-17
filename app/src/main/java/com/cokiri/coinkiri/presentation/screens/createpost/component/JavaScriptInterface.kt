package com.cokiri.coinkiri.presentation.screens.createpost.component

import android.util.Log
import android.webkit.JavascriptInterface
import com.cokiri.coinkiri.presentation.createpost.CreatePostViewModel
import org.json.JSONException
import org.json.JSONObject

/**
 * WebView에서 JavaScript와의 상호작용을 위한 인터페이스
 * JavaScript에서 AndroidInterface.receiveContent(content)로 호출할 수 있다.
 */
class JavaScriptInterface(
    private val viewModel: CreatePostViewModel
) {
    @JavascriptInterface
    fun receiveContent(content: String) {
        handleReceivedContent(content, viewModel)
    }
}

private fun handleReceivedContent(
    content: String,
    createPostViewModel: CreatePostViewModel
) {
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
    } catch (e: JSONException) {
        e.printStackTrace()
        Log.e("handleReceivedContent", "JSON parsing error: ${e.message}")
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("handleReceivedContent", "Error: ${e.message}")
    }
}