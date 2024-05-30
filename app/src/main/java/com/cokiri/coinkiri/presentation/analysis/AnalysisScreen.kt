package com.cokiri.coinkiri.presentation.analysis

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.presentation.analysis.component.AnalysisCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("분석") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(
                            painter = painterResource(R.drawable.ic_navi_home),
                            contentDescription = "",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            )
        },
        content = {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(10) {
                    AnalysisCard()
                }
            }
        },
        floatingActionButton = {}
    )
}