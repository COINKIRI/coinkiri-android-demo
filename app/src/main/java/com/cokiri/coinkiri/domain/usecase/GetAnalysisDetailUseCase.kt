package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.AnalysisDetailResponseDto
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

class GetAnalysisDetailUseCase @Inject constructor(
    private val postRepository: PostRepository
){
    suspend operator fun invoke(postId: Long): Result<AnalysisDetailResponseDto> {
        return runCatching { postRepository.getAnalysisPostDetail(postId) }
    }
}