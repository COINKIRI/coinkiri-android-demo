package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommentList
import com.cokiri.coinkiri.data.remote.model.CommentRequest

interface CommentRepository {
    suspend fun getCommentList(postId: Long) : List<CommentList>

    suspend fun submitComment(commentRequest : CommentRequest) : Result<ApiResponse>
}