package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.AnalysisPostDataRequest
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

class SubmitAnalysisPostContentUseCase @Inject constructor(
    private val postRepository: PostRepository
){
    suspend operator fun invoke(analysisPostDataRequest: AnalysisPostDataRequest): Result<ApiResponse> {
        return runCatching { postRepository.submitAnalysisPost(analysisPostDataRequest) }
    }
}