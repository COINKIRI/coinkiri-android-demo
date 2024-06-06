package com.cokiri.coinkiri.domain.usecase.post

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.PostRequestDto
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

/**
 * 작성된 글을 서버에 전송하는 UseCase
 */
class SubmitPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postRequestDto: PostRequestDto): Result<ApiResponse> {
        return runCatching { postRepository.submitPost(postRequestDto) }
    }
}