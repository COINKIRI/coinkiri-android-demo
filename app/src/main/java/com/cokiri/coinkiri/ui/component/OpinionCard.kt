package com.cokiri.coinkiri.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen

/**
 * OpinionCard
 * - 분석글 작성 페이지에서 사용되는 의견 카드
 */
@Composable
fun OpinionCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(CoinkiriBackground),
        border = BorderStroke(
            when {
                isSelected -> 2.dp
                else -> 1.dp
            },
            when {
                isSelected -> CoinkiriPointGreen
                else -> Color.LightGray
            }
        ),
        shape = shape,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = text)
        }
    }
}

