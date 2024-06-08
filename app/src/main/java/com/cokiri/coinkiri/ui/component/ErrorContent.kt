package com.cokiri.coinkiri.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * 에러 상태일 때 에러 메시지를 표시하는 Composable
 */
@Composable
fun ErrorContent(
    paddingValues: PaddingValues,
    errorMessage: String?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Text(text = errorMessage ?: "Unknown error", color = Color.Red)
    }
}