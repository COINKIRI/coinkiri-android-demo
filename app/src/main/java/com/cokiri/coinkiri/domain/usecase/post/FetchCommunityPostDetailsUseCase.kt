package com.cokiri.coinkiri.domain.usecase.post

import com.cokiri.coinkiri.data.remote.model.post.community.CommunityDetailResponseDto
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

/**
 * 커뮤니티 게시글 상세 정보를 가져오는 UseCase
 */
class FetchCommunityPostDetailsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: Long): Result<CommunityDetailResponseDto> {
        return runCatching { postRepository.getCommunityPostDetail(postId) }
    }
}