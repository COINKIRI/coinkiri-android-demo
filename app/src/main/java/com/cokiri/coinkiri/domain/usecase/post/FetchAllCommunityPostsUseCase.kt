package com.cokiri.coinkiri.domain.usecase.post

import com.cokiri.coinkiri.data.remote.model.post.community.CommunityResponseDto
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

/**
 * 커뮤니티 게시글 목록을 가져오는 UseCase
 */
class FetchAllCommunityPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(): Result<List<CommunityResponseDto>> {
        return runCatching { postRepository.getCommunityPostList() }
    }
}