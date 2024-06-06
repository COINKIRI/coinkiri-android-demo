package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.CommunityResponseDto
import com.cokiri.coinkiri.data.remote.model.NewsList
import com.cokiri.coinkiri.data.remote.model.PostRequestDto

interface PostRepository {

    /**
     * 게시글 작성 요청
     */
    suspend fun submitPost(postRequestDto: PostRequestDto) : ApiResponse

    /**
     * 커뮤니티 전체 게시글 목록 조회
     */
    suspend fun getCommunityPostList(forceRefresh: Boolean = false): List<CommunityResponseDto>

    /**
     * 뉴스 전체 목록 조회
     */
    suspend fun getNewsList(forceRefresh: Boolean = false): List<NewsList>

    /**
     * 커뮤니티 게시글 상세 조회
     */
    suspend fun getCommunityPostDetail(postId: Long) : CommunityDetailResponseDto

}