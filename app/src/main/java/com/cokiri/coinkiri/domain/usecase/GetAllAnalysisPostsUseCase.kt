package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.AnalysisResponseDto
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

class GetAllAnalysisPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(): Result<List<AnalysisResponseDto>> {
        return runCatching { postRepository.getAnalysisPostList() }
    }
}