package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

class GetCommunityPostDetailUseCase @Inject constructor(
    private val postRepository: PostRepository
){
    suspend operator fun invoke(postId: Long) : CommunityDetailResponseDto {
        return postRepository.getCommunityPostDetail(postId)
    }
}