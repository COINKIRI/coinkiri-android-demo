package com.cokiri.coinkiri.presentation.screens.price

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.data.remote.model.coin.CoinTalk
import com.cokiri.coinkiri.presentation.screens.price.component.CommentCard
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CoinTalkScreen(
    coinTalkList: List<CoinTalk>,
    coinTalkContent: String,
    onCoinTalkContentChanged: (String) -> Unit,
    onSubmitClick: () -> Unit
) {
    Scaffold(
        content = { paddingValues ->
            CoinTalkContent(
                paddingValues = paddingValues,
                coinTalkList = coinTalkList
            )
        },
        bottomBar = {
            CoinTalkBottomBar(
                value = coinTalkContent,
                onValueChange = onCoinTalkContentChanged,
                onSubmitClick = onSubmitClick
            )
        }
    )
}

@Composable
fun CoinTalkContent(
    paddingValues: PaddingValues,
    coinTalkList: List<CoinTalk>
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        items(coinTalkList.size) { index ->
            val coinTalk = coinTalkList[index]
            CommentCard(coinTalk = coinTalk)
        }
    }
}


@Composable
fun CoinTalkBottomBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmitClick: () -> Unit
) {
    BottomAppBar(
        containerColor = CoinkiriWhite,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                shape = RoundedCornerShape(30.dp),
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                placeholder = {
                    Text(
                        text = "댓글을 입력하세요.",
                        fontFamily = PretendardFont,
                        fontSize = 12.sp,
                    )
                },
            )
            IconButton(
                onClick = onSubmitClick,
                enabled = value.isNotEmpty()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    tint = if (value.isNotEmpty()) CoinkiriPointGreen else Color.Gray, // 비활성화 시 회색으로 변경
                    contentDescription = "전송",
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}