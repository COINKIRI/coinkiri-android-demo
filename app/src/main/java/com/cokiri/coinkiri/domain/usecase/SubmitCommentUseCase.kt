package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommentRequest
import com.cokiri.coinkiri.domain.repository.CommentRepository
import javax.inject.Inject

class SubmitCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
){
    suspend operator fun invoke(commentRequest: CommentRequest): Result<ApiResponse> {
        return commentRepository.submitComment(commentRequest)
    }
}