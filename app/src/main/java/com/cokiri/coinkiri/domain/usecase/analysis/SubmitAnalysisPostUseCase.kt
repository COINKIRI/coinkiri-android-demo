package com.cokiri.coinkiri.domain.usecase.analysis

import com.cokiri.coinkiri.data.remote.model.AnalysisPostDataRequest
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.domain.repository.AnalysisRepository
import javax.inject.Inject

/**
 * 작성된 분석글을 서버에 전송하는 UseCase
 */
class SubmitAnalysisPostUseCase @Inject constructor(
    private val analysisRepository: AnalysisRepository
){
    suspend operator fun invoke(analysisPostDataRequest: AnalysisPostDataRequest): Result<ApiResponse> {
        return runCatching { analysisRepository.submitAnalysisPost(analysisPostDataRequest) }
    }
}