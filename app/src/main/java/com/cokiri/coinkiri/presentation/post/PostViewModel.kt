package com.cokiri.coinkiri.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.CommunityResponseDto
import com.cokiri.coinkiri.data.remote.model.NewsList
import com.cokiri.coinkiri.domain.usecase.post.FetchCommunityPostDetailsUseCase
import com.cokiri.coinkiri.domain.usecase.post.FetchAllCommunityPostsUseCase
import com.cokiri.coinkiri.domain.usecase.post.FetchAllNewsUseCase
import com.cokiri.coinkiri.extensions.executeWithLoading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val fetchAllCommunityPostsUseCase: FetchAllCommunityPostsUseCase,
    private val fetchCommunityPostDetailsUseCase: FetchCommunityPostDetailsUseCase,
    private val fetchAllNewsUseCase: FetchAllNewsUseCase
) : ViewModel() {

    // 커뮤니티 게시글 목록을 관리하는 MutableStateFlow
    private val _communityPostList = MutableStateFlow<List<CommunityResponseDto>>(emptyList())
    val communityPostList: StateFlow<List<CommunityResponseDto>> = _communityPostList

    // 커뮤니티 게시글 상세 정보를 관리하는 MutableStateFlow
    private val _communityDetail = MutableStateFlow<CommunityDetailResponseDto?>(null)
    val communityDetail: StateFlow<CommunityDetailResponseDto?> = _communityDetail

    // 뉴스 목록을 관리하는 MutableStateFlow
    private val _newsList = MutableStateFlow<List<NewsList>>(emptyList())
    val newsList: StateFlow<List<NewsList>> = _newsList

    // 로딩 상태를 관리하는 MutableStateFlow
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // 에러 메시지를 관리하는 MutableStateFlow
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    /**
     * 커뮤니티 게시글 목록을 가져오는 함수
     */
    fun fetchAllCommunityPostList() {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                val result = fetchAllCommunityPostsUseCase()
                if (result.isSuccess) {
                    _communityPostList.value = result.getOrDefault(emptyList())
                }
                result
            }
        }
    }


    /**
     * 커뮤니티 게시글 상세 정보를 가져오는 함수
     * @param postId 가져올 게시글의 ID
     */
    suspend fun fetchCommunityPostDetails(postId: Long) {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                val result = fetchCommunityPostDetailsUseCase(postId)
                if (result.isSuccess) {
                    _communityDetail.value = result.getOrNull()
                }
                result
            }
        }
    }


    /**
     * 뉴스 목록을 가져오는 함수
     */
    fun fetchAllNewsList() {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                val result = fetchAllNewsUseCase()
                if (result.isSuccess) {
                    _newsList.value = result.getOrDefault(emptyList())
                }
                result
            }
        }
    }
}
