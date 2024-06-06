package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.AnalysisResponseDto
import com.cokiri.coinkiri.domain.repository.AnalysisRepository
import javax.inject.Inject

class GetAllAnalysisPostsUseCase @Inject constructor(
    private val analysisRepository: AnalysisRepository
) {
    suspend operator fun invoke(): Result<List<AnalysisResponseDto>> {
        return runCatching { analysisRepository.getAnalysisPostList() }
    }
}