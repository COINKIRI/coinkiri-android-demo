package com.cokiri.coinkiri.data.remote.model.comment

import com.squareup.moshi.JsonClass

/**
 * 댓글 작성 요청
 */
@JsonClass(generateAdapter = true)
data class CommentRequest(
    val postId: Long,       // 댓글이 달릴 게시물의 고유 ID
    val content: String     // 댓글의 내용
)