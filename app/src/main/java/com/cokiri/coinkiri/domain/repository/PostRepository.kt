package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.PostDataRequest

interface PostRepository {
    suspend fun submitPost(postDataRequest: PostDataRequest) : Result<ApiResponse>
}