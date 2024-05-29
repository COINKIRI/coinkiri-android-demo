package com.cokiri.coinkiri.presentation.post

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.presentation.post.community.CommentCard
import com.cokiri.coinkiri.ui.theme.CoinkiriBackground

//@Preview
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CommentModalBottomSheet(
//    //onCloseModalBottomSheet : () -> Unit
//) {
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("댓글") },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(CoinkiriBackground),
//                navigationIcon = {
//                    IconButton(
//                        onClick = {/*TODO()*/}
//                        //onCloseModalBottomSheet
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_navi_home),
//                            contentDescription = "닫기"
//                        )
//                    }
//                },
//            )
//        },
//        content = {
//            LazyColumn(
//                modifier = Modifier.padding(it)
//            ) {
//                items(5) {
//                    CommentCard(it)
//                }
//            }
//        },
//        bottomBar = {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                var commentText by remember { mutableStateOf("") }
//
//                TextField(
//                    value = commentText,
//                    onValueChange = { commentText = it },
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(end = 8.dp),
//                    placeholder = { Text("댓글을 입력하세요...") }
//                )
//                Button(
//                    onClick = {
//                        commentText = ""
//                    }
//                ) {
//                    Text("제출")
//                }
//            }
//        }
//    )
//}
//
