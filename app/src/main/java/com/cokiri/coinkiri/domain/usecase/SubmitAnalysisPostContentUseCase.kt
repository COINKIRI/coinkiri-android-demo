package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.AnalysisPostDataRequest
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.domain.repository.AnalysisRepository
import javax.inject.Inject

class SubmitAnalysisPostContentUseCase @Inject constructor(
    private val analysisRepository: AnalysisRepository
){
    suspend operator fun invoke(analysisPostDataRequest: AnalysisPostDataRequest): Result<ApiResponse> {
        return runCatching { analysisRepository.submitAnalysisPost(analysisPostDataRequest) }
    }
}