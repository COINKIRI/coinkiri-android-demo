package com.cokiri.coinkiri.presentation.price

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.data.remote.model.coin.CoinTalk
import com.cokiri.coinkiri.presentation.price.component.CommentCard
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CoinTalkScreen(
    coinId: Long,
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
                        fontSize = 12.sp,
                    )
                },
            )
            IconButton(onClick = onSubmitClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navi_home),
                    contentDescription = "전송"
                )
            }
        }
    }
}
