package com.cokiri.coinkiri.presentation.post

import android.util.Log
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.CommunityResponseDto
import com.cokiri.coinkiri.data.remote.model.NewsList
import com.cokiri.coinkiri.domain.usecase.post.FetchAllCommunityPostsUseCase
import com.cokiri.coinkiri.domain.usecase.post.FetchAllNewsUseCase
import com.cokiri.coinkiri.domain.usecase.post.FetchCommunityPostDetailsUseCase
import com.cokiri.coinkiri.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val fetchAllCommunityPostsUseCase: FetchAllCommunityPostsUseCase,
    private val fetchCommunityPostDetailsUseCase: FetchCommunityPostDetailsUseCase,
    private val fetchAllNewsUseCase: FetchAllNewsUseCase
) : BaseViewModel() {

    // 커뮤니티 게시글 목록을 관리하는 MutableStateFlow
    private val _communityPostList = MutableStateFlow<List<CommunityResponseDto>>(emptyList())
    val communityPostList: StateFlow<List<CommunityResponseDto>> = _communityPostList

    // 커뮤니티 게시글 상세 정보를 관리하는 MutableStateFlow
    private val _communityDetail = MutableStateFlow<CommunityDetailResponseDto?>(null)
    val communityDetail: StateFlow<CommunityDetailResponseDto?> = _communityDetail

    // 뉴스 목록을 관리하는 MutableStateFlow
    private val _newsList = MutableStateFlow<List<NewsList>>(emptyList())
    val newsList: StateFlow<List<NewsList>> = _newsList

    /**
     * 커뮤니티 게시글 목록을 가져오는 함수
     */
    fun fetchAllCommunityPostList() {
        executeWithLoading(
            block = { fetchAllCommunityPostsUseCase() },
            onSuccess = { result ->
                _communityPostList.value = result
            },
            onFailure = { error ->
                Log.d("PostViewModel", "error: $error")
                _communityPostList.value = emptyList()
            }
        )
    }


    /**
     * 커뮤니티 게시글 상세 정보를 가져오는 함수
     * @param postId 가져올 게시글의 ID
     */
    suspend fun fetchCommunityPostDetails(postId: Long) {
        executeWithLoading(
            block = { fetchCommunityPostDetailsUseCase(postId) },
            onSuccess = { result ->
                _communityDetail.value = result
            },
            onFailure = { error ->
                Log.d("PostViewModel", "error: $error")
                _communityDetail.value = null
            }
        )
    }


    /**
     * 뉴스 목록을 가져오는 함수
     */
    fun fetchAllNewsList() {
        executeWithLoading(
            block = { fetchAllNewsUseCase() },
            onSuccess = { result ->
                _newsList.value = result
            },
            onFailure = { error ->
                Log.d("PostViewModel", "error: $error")
                _newsList.value = emptyList()
            }
        )
    }
}
