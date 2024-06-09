package com.cokiri.coinkiri.domain.usecase.post

import com.cokiri.coinkiri.data.remote.model.post.community.CommunityResponseDto
import com.cokiri.coinkiri.domain.repository.PostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchUserCommunityListUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    fun execute(callback: (Result<List<CommunityResponseDto>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = runCatching { postRepository.fetchUserCommunityList() }
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }
}