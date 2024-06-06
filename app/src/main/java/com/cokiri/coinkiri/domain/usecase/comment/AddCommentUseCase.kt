package com.cokiri.coinkiri.domain.usecase.comment

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.comment.CommentRequest
import com.cokiri.coinkiri.domain.repository.CommentRepository
import javax.inject.Inject

/**
 * 댓글 작성 UseCase
 */
class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
){
    suspend operator fun invoke(commentRequest: CommentRequest): Result<ApiResponse> {
        return runCatching { commentRepository.submitComment(commentRequest) }
    }
}