package com.cokiri.coinkiri.domain.usecase.like

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.domain.repository.LikeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteLikeUseCase @Inject constructor(
    private val likeRepository: LikeRepository
){
    fun execute(postId: Long, callback: (Result<ApiResponse>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = runCatching { likeRepository.deleteLike(postId) }
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }
}