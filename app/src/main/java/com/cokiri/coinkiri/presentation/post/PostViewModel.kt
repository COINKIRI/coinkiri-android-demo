package com.cokiri.coinkiri.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommentList
import com.cokiri.coinkiri.data.remote.model.CommentRequest
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.CommunityResponseDto
import com.cokiri.coinkiri.domain.usecase.GetAllCommentsUseCase
import com.cokiri.coinkiri.domain.usecase.GetCommunityPostDetailsUseCase
import com.cokiri.coinkiri.domain.usecase.GetAllCommunityPostsUseCase
import com.cokiri.coinkiri.domain.usecase.SubmitCommentUseCase
import com.cokiri.coinkiri.extensions.executeWithLoading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getAllCommunityPostsUseCase: GetAllCommunityPostsUseCase,
    private val getCommunityPostDetailsUseCase: GetCommunityPostDetailsUseCase,
    private val getAllCommentsUseCase: GetAllCommentsUseCase,
    private val submitCommentUseCase: SubmitCommentUseCase
) : ViewModel() {

    // 커뮤니티 게시글 목록을 관리하는 MutableStateFlow
    private val _communityPostList = MutableStateFlow<List<CommunityResponseDto>>(emptyList())
    val communityPostList: StateFlow<List<CommunityResponseDto>> = _communityPostList

    // 커뮤니티 게시글 상세 정보를 관리하는 MutableStateFlow
    private val _communityDetail = MutableStateFlow<CommunityDetailResponseDto?>(null)
    val communityDetail: StateFlow<CommunityDetailResponseDto?> = _communityDetail

    // 로딩 상태를 관리하는 MutableStateFlow
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // 에러 메시지를 관리하는 MutableStateFlow
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // 댓글 목록을 관리하는 MutableStateFlow
    private val _commentList = MutableStateFlow<List<CommentList>>(emptyList())
    val commentList: StateFlow<List<CommentList>> = _commentList

    // 작성 중인 댓글 내용을 관리하는 MutableStateFlow
    private val _commentContent = MutableStateFlow("")
    val commentContent: StateFlow<String> = _commentContent

    // 댓글 작성 결과를 관리하는 MutableStateFlow
    private val _submitCommentResult = MutableStateFlow<Result<ApiResponse>?>(null)



    /**
     * 댓글을 작성하는 함수
     * @param postId 댓글을 작성할 게시글의 ID
     */
    fun submitComment(postId: Long) {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                val content = _commentContent.value
                if (content.isBlank()) {
                    throw IllegalArgumentException("댓글 내용을 입력해주세요.")
                }

                val commentRequest = CommentRequest(postId, content)
                val result = submitCommentUseCase(commentRequest)
                _submitCommentResult.value = result

                if (result.isSuccess) {
                    fetchCommentList(postId)
                }
                result
            }
        }
    }


    /**
     * 댓글 목록을 가져오는 함수
     * @param postId 댓글 목록을 가져올 게시글의 ID
     */
    suspend fun fetchCommentList(postId: Long) {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                val result = getAllCommentsUseCase(postId)
                if (result.isSuccess) {
                    _commentList.value = result.getOrDefault(emptyList())
                }
                result
            }
        }
    }


    /**
     * 커뮤니티 게시글 목록을 가져오는 함수
     */
    fun fetchAllCommunityPostList() {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                val result = getAllCommunityPostsUseCase()
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
                val result = getCommunityPostDetailsUseCase(postId)
                if (result.isSuccess) {
                    _communityDetail.value = result.getOrNull()
                }
                result
            }
        }
    }


    /**
     * 댓글 내용이 변경될 때 호출되는 함수
     */
    fun onCommentContentChange(newCommentContent: String) {
        _commentContent.value = newCommentContent
    }
}
