package com.cokiri.coinkiri.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 커스텀 스낵바 호스트
 */
@Composable
fun CustomSnackbarHost(snackbarHostState: SnackbarHostState) {
    SnackbarHost(hostState = snackbarHostState) { data ->
        Snackbar(
            shape = RoundedCornerShape(10.dp),
            snackbarData = data,
            containerColor = Color.Black,
            contentColor = Color.White,
        )
    }
}