package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.remote.model.AnalysisDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.AnalysisPostDataRequest
import com.cokiri.coinkiri.data.remote.model.AnalysisResponseDto
import com.cokiri.coinkiri.data.remote.model.ApiResponse

interface AnalysisRepository {

    /**
     * 분석 게시글 작성 요청
     */
    suspend fun submitAnalysisPost(analysisPostDataRequest: AnalysisPostDataRequest) : ApiResponse

    /**
     * 분석 전체 게시글 목록 조회
     */
    suspend fun getAnalysisPostList(forceRefresh: Boolean = false): List<AnalysisResponseDto>

    /**
     * 분석 게시글 상세 조회
     */
    suspend fun getAnalysisPostDetail(postId: Long) : AnalysisDetailResponseDto

}