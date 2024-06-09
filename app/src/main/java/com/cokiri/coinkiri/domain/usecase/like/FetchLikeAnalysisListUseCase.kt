package com.cokiri.coinkiri.domain.usecase.like

import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisResponseDto
import com.cokiri.coinkiri.domain.repository.LikeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchLikeAnalysisListUseCase @Inject constructor(
    private val likeRepository: LikeRepository
){
    fun execute(callback: (Result<List<AnalysisResponseDto>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = runCatching { likeRepository.fetchLikeAnalysisList() }
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }
}