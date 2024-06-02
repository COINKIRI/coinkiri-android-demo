package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.remote.model.AnalysisPostDataRequest
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.CommunityResponseDto
import com.cokiri.coinkiri.data.remote.model.PostRequestDto

interface PostRepository {
    suspend fun submitPost(postRequestDto: PostRequestDto) : ApiResponse

    suspend fun submitAnalysisPost(analysisPostDataRequest: AnalysisPostDataRequest) : ApiResponse

    suspend fun getCommunityPostList(forceRefresh: Boolean = false): List<CommunityResponseDto>

    suspend fun getCommunityPostDetail(postId: Long) : CommunityDetailResponseDto
}