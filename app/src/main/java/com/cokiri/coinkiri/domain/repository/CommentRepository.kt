package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.comment.CommentList
import com.cokiri.coinkiri.data.remote.model.comment.CommentRequest

interface CommentRepository {
    suspend fun getCommentList(postId: Long) : List<CommentList>

    suspend fun submitComment(commentRequest : CommentRequest) : ApiResponse
}