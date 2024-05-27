package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.CommunityResponseDto
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

class GetCommunityPostListUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(): List<CommunityResponseDto> {
        return postRepository.getCommunityPostList()
    }

}

