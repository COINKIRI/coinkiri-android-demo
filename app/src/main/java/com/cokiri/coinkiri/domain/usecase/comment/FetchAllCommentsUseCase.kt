package com.cokiri.coinkiri.domain.usecase.comment

import com.cokiri.coinkiri.data.remote.model.CommentList
import com.cokiri.coinkiri.domain.repository.CommentRepository
import javax.inject.Inject

/**
 * 댓글 목록을 가져오는 UseCase
 */
class FetchAllCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository
){
    suspend operator fun invoke(postId: Long): Result<List<CommentList>> {
        return runCatching { commentRepository.getCommentList(postId) }
    }
}