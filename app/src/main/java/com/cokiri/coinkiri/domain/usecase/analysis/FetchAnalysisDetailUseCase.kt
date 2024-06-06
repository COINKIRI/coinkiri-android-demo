package com.cokiri.coinkiri.domain.usecase.analysis

import com.cokiri.coinkiri.data.remote.model.AnalysisDetailResponseDto
import com.cokiri.coinkiri.domain.repository.AnalysisRepository
import javax.inject.Inject

/**
 * 분석글 상세 정보를 가져오는 UseCase
 */
class FetchAnalysisDetailUseCase @Inject constructor(
    private val analysisRepository: AnalysisRepository
){
    suspend operator fun invoke(postId: Long): Result<AnalysisDetailResponseDto> {
        return runCatching { analysisRepository.getAnalysisPostDetail(postId) }
    }
}