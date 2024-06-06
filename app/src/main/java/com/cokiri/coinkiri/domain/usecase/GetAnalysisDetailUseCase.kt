package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.AnalysisDetailResponseDto
import com.cokiri.coinkiri.domain.repository.AnalysisRepository
import javax.inject.Inject

class GetAnalysisDetailUseCase @Inject constructor(
    private val analysisRepository: AnalysisRepository
){
    suspend operator fun invoke(postId: Long): Result<AnalysisDetailResponseDto> {
        return runCatching { analysisRepository.getAnalysisPostDetail(postId) }
    }
}