package com.cokiri.coinkiri.domain.usecase.analysis

import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisResponseDto
import com.cokiri.coinkiri.domain.repository.AnalysisRepository
import javax.inject.Inject

/**
 * 모든 분석글을 가져오는 UseCase
 */
class FetchAllAnalysisPostsUseCase @Inject constructor(
    private val analysisRepository: AnalysisRepository
) {
    suspend operator fun invoke(): Result<List<AnalysisResponseDto>> {
        return runCatching { analysisRepository.getAnalysisPostList() }
    }
}