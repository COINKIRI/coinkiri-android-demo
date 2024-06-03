package com.cokiri.coinkiri.presentation.createpost

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground


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

