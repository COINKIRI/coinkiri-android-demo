package com.cokiri.coinkiri.ui.component.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite

/**
 * 게시글(분석,커뮤니티) 상세화면의 TopAppBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(
    backClick: () -> Unit
) {
    Surface(
        color = CoinkiriWhite,
        shadowElevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        TopAppBar(
            title = { },
            colors = TopAppBarDefaults.topAppBarColors(CoinkiriWhite),
            navigationIcon = {
                IconButton(onClick = backClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "뒤로가기"
                    )
                }
            },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_post_baseline_visibility),
                        contentDescription = "더보기"
                    )
                }
            }
        )
    }
}