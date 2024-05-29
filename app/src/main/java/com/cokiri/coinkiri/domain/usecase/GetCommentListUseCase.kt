package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.CommentList
import com.cokiri.coinkiri.domain.repository.CommentRepository
import javax.inject.Inject

class GetCommentListUseCase @Inject constructor(
    private val commentRepository: CommentRepository
){
    suspend operator fun invoke(postId: Long) : List<CommentList> {
        return commentRepository.getCommentList(postId)
    }
}