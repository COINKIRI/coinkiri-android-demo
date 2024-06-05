package com.cokiri.coinkiri.domain.usecase

import com.cokiri.coinkiri.data.remote.model.NewsList
import com.cokiri.coinkiri.domain.repository.PostRepository
import javax.inject.Inject

class GetAllNewsUseCase @Inject constructor(
    private val postRepository: PostRepository
){
    suspend operator fun invoke(): Result<List<NewsList>> {
        return runCatching { postRepository.getNewsList() }
    }
}