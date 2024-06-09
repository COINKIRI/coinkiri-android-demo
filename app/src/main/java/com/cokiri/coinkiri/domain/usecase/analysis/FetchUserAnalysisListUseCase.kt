package com.cokiri.coinkiri.domain.usecase.analysis

import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisResponseDto
import com.cokiri.coinkiri.domain.repository.AnalysisRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchUserAnalysisListUseCase @Inject constructor(
    private val analysisRepository: AnalysisRepository
) {
    fun execute(callback: (Result<List<AnalysisResponseDto>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = runCatching { analysisRepository.fetchUserAnalysisList() }
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }
}