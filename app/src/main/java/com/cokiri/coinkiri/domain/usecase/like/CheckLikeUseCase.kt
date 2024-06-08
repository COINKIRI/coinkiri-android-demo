package com.cokiri.coinkiri.domain.usecase.like

import com.cokiri.coinkiri.domain.repository.LikeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckLikeUseCase @Inject constructor(
    private val likeRepository: LikeRepository
) {
    fun execute(postId: Long, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = runCatching { likeRepository.checkLike(postId) }
                .getOrElse {
                    // 예외 발생 시 false 반환
                    false
                }
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }
}