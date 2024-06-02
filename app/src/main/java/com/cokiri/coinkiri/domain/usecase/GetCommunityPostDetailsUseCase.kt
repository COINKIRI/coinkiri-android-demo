package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

/**
 * 커뮤니티 게시글 상세 정보를 가져오는 UseCase
 */
class GetCommunityPostDetailsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: Long): Result<CommunityDetailResponseDto> {
        return runCatching { postRepository.getCommunityPostDetail(postId) }
    }
}