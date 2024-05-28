package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.CommunityResponseDto
import com.cokiri.coinkiri.data.remote.model.PostDataRequest
import com.cokiri.coinkiri.data.remote.model.PostResponseDto

interface PostRepository {
    suspend fun submitPost(postDataRequest: PostDataRequest) : Result<ApiResponse>

    suspend fun getCommunityPostList() : List<CommunityResponseDto>

    suspend fun getCommunityPostDetail(postId: Long) : CommunityDetailResponseDto
}