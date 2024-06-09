package com.cokiri.coinkiri.domain.usecase.like

import com.cokiri.coinkiri.data.remote.model.post.community.CommunityResponseDto
import com.cokiri.coinkiri.domain.repository.LikeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchLikeCommunityListUseCase @Inject constructor(
    private val likeRepository: LikeRepository
){
    fun execute(callback: (Result<List<CommunityResponseDto>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = runCatching { likeRepository.fetchLikeCommunityList() }
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }

}