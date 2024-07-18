package com.cokiri.coinkiri.presentation.screens.comment

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cokiri.coinkiri.R
import com.cokiri.coinkiri.data.remote.model.comment.CommentList
import com.cokiri.coinkiri.presentation.screens.comment.component.CommentCard
import com.cokiri.coinkiri.ui.theme.CoinkiriPointGreen
import com.cokiri.coinkiri.ui.theme.CoinkiriWhite
import com.cokiri.coinkiri.ui.theme.PretendardFont

@Composable
fun CommentScreen(
    commentViewModel : CommentViewModel = hiltViewModel(),
    closeClick: () -> Unit,
    postId: Long
) {

    LaunchedEffect(postId) {
        commentViewModel.fetchCommentList(postId)
    }

    val commentList by commentViewModel.commentList.collectAsStateWithLifecycle()
    val commentContent by commentViewModel.commentContent.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CommentTopBar(closeClick = closeClick)
        },
        content = { paddingValues ->
            CommentContent(
                paddingValues = paddingValues,
                commentList = commentList
            )
        },
        bottomBar = {
            CommentBottomBar(
                value = commentContent,
                onValueChange = { newCommentContent ->
                    commentViewModel.onCommentContentChange(
                        newCommentContent
                    )
                },
                onSubmitClick = {
                    commentViewModel.submitComment(postId)
                    commentViewModel.onCommentContentChange("")
                }
            )
        }
    )
}


/**
 * 댓글 작성화면의 TopBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentTopBar(
    closeClick: () -> Unit
) {
    Surface(
        color = CoinkiriWhite,
        shadowElevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()

    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "댓글",
                    fontFamily = PretendardFont,
                    fontSize = 18.sp,
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(CoinkiriWhite),
            navigationIcon = {
                IconButton(
                    onClick = closeClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "닫기"
                    )
                }
            },
        )
    }
}


/**
 * 댓글 작성화면의 content
 */
@Composable
fun CommentContent(
    paddingValues: PaddingValues,
    commentList: List<CommentList>
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        items(commentList.size) { index ->
            val comment = commentList[index]
            CommentCard(comment)
        }
    }
}


/**
 * 댓글 작성화면의 BottomBar
 */
@Composable
fun CommentBottomBar(
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
