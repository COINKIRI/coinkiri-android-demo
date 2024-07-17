package com.cokiri.coinkiri.presentation.screens.post

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.post.community.CommunityDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.post.community.CommunityResponseDto
import com.cokiri.coinkiri.data.remote.model.post.news.NewsList
import com.cokiri.coinkiri.domain.usecase.like.FetchLikeCommunityListUseCase
import com.cokiri.coinkiri.domain.usecase.post.FetchAllCommunityPostsUseCase
import com.cokiri.coinkiri.domain.usecase.post.FetchAllNewsUseCase
import com.cokiri.coinkiri.domain.usecase.post.FetchCommunityPostDetailsUseCase
import com.cokiri.coinkiri.domain.usecase.post.FetchUserCommunityListUseCase
import com.cokiri.coinkiri.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val fetchAllCommunityPostsUseCase: FetchAllCommunityPostsUseCase,
    private val fetchCommunityPostDetailsUseCase: FetchCommunityPostDetailsUseCase,
    private val fetchAllNewsUseCase: FetchAllNewsUseCase,
    private val fetchLikeCommunityListUseCase: FetchLikeCommunityListUseCase,
    private val fetchUserCommunityListUseCase: FetchUserCommunityListUseCase
) : BaseViewModel() {

    // 커뮤니티 게시글 목록을 관리하는 MutableStateFlow
    private val _communityPostList = MutableStateFlow<List<CommunityResponseDto>>(emptyList())
    val communityPostList: StateFlow<List<CommunityResponseDto>> = _communityPostList

    // 커뮤니티 게시글 상세 정보를 관리하는 MutableStateFlow
    private val _communityDetail = MutableStateFlow<CommunityDetailResponseDto?>(null)
    val communityDetail: StateFlow<CommunityDetailResponseDto?> = _communityDetail

    // 좋아요 목록을 관리하는 MutableStateFlow
    private val _likeList = MutableStateFlow<List<CommunityResponseDto>>(emptyList())
    val likeList: StateFlow<List<CommunityResponseDto>> = _likeList

    // 커뮤니티 작성글 목록을 관리하는 MutableStateFlow
    private val _userCommunityList = MutableStateFlow<List<CommunityResponseDto>>(emptyList())
    val userCommunity : StateFlow<List<CommunityResponseDto>> = _userCommunityList



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
     * 좋아요한 커뮤니티 게시글 목록을 가져오는 함수
     */
    fun fetchLikeCommunityList() {
        viewModelScope.launch {
            fetchLikeCommunityListUseCase.execute { result ->
                if (result.isSuccess) {
                    _likeList.value = result.getOrNull() ?: emptyList()
                }
            }
        }
    }


    /**
     *  작성한 커뮤니티 게시글 목록을 가져오는 함수
     */
    fun fetchUserCommunityList() {
        viewModelScope.launch {
            fetchUserCommunityListUseCase.execute { result ->
                if (result.isSuccess){
                    _userCommunityList.value = result.getOrNull() ?: emptyList()
                }
            }
        }
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
