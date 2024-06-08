package com.cokiri.coinkiri.domain.repository

import com.cokiri.coinkiri.data.remote.model.ApiResponse

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
}