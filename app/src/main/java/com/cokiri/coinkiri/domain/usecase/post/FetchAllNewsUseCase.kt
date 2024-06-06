package com.cokiri.coinkiri.domain.usecase.post

import com.cokiri.coinkiri.data.remote.model.NewsList
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

/**
 * 모든 뉴스 게시글을 가져오는 UseCase
 */
class FetchAllNewsUseCase @Inject constructor(
    private val postRepository: PostRepository
){
    suspend operator fun invoke(): Result<List<NewsList>> {
        return runCatching { postRepository.getNewsList() }
    }
}