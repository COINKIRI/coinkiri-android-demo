package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisResponseDto
import com.cokiri.coinkiri.data.remote.model.post.community.CommunityResponseDto

interface LikeRepository {

    /**
     * 좋아요를 누름
     */
    suspend fun addLike(postId: Long): ApiResponse

    /**
     * 좋아요를 취소함
     */
    suspend fun deleteLike(postId: Long): ApiResponse

    /**
     * 좋아요 여부 확인
     */
    suspend fun checkLike(postId: Long): Boolean


    /**
     * 좋아요 한 게시물 리스트
     */
    suspend fun fetchLikeCommunityList(forceRefresh: Boolean = false): List<CommunityResponseDto>


    /**
     * 좋아요 한 분석글 리스트
     */
    suspend fun fetchLikeAnalysisList(forceRefresh: Boolean = false): List<AnalysisResponseDto>
}