package com.cokiri.coinkiri.presentation.post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommentList
import com.cokiri.coinkiri.data.remote.model.CommentRequest
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.CommunityResponseDto
import com.cokiri.coinkiri.data.remote.model.ImageData
import com.cokiri.coinkiri.data.remote.model.PostDataRequest
import com.cokiri.coinkiri.domain.usecase.GetCommentListUseCase
import com.cokiri.coinkiri.domain.usecase.GetCommunityPostDetailUseCase
import com.cokiri.coinkiri.domain.usecase.GetCommunityPostListUseCase
import com.cokiri.coinkiri.domain.usecase.SubmitCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getCommunityPostListUseCase: GetCommunityPostListUseCase,
    private val getCommunityPostDetailUseCase: GetCommunityPostDetailUseCase,
    private val getCommentListUseCase: GetCommentListUseCase,
    private val submitCommentUseCase: SubmitCommentUseCase
) : ViewModel() {

    private val _communityPostList = MutableStateFlow<List<CommunityResponseDto>>(emptyList())
    val communityPostList: StateFlow<List<CommunityResponseDto>> = _communityPostList

    private val _communityDetail = MutableStateFlow<CommunityDetailResponseDto?>(null)
    val communityDetail: StateFlow<CommunityDetailResponseDto?> = _communityDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _commentList = MutableStateFlow<List<CommentList>>(emptyList())
    val commentList: StateFlow<List<CommentList>> = _commentList

    private val _commentContent = MutableStateFlow("")
    val commentContent: StateFlow<String> = _commentContent

    private val _submitResult = MutableStateFlow<Result<ApiResponse>?>(null)


    fun onCommentContentChange(newCommentContent: String) {
        _commentContent.value = newCommentContent
        Log.d("PostViewModel", "onCommentContentChange: ${_commentContent.value}")
    }

    fun submitComment(postId: Long) {
        viewModelScope.launch {
            try {
                val content = _commentContent.value
                if (content.isBlank()) {
                    _errorMessage.value = "Content cannot be blank"
                    return@launch
                }
                val commentRequest = CommentRequest(
                    postId = postId,
                    content = content
                )
                _submitResult.value = submitCommentUseCase(commentRequest)
                _submitResult.value?.let {
                    if (it.isSuccess) {
                        fetchCommentList(postId)
                    } else {
                        _errorMessage.value = "댓글 작성에 실패하였습니다."
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }



    fun fetchCommunityPostList() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _communityPostList.value = getCommunityPostListUseCase()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun fetchCommunityPostDetail(postId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                Log.d("PostViewModel", "communityDetail: ${_communityDetail.value}")
                _communityDetail.value = getCommunityPostDetailUseCase(postId)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun fetchCommentList(postId: Long) {
        _isLoading.value = true
        _errorMessage.value = null
        try {
            _commentList.value = getCommentListUseCase(postId)
            Log.d("PostViewModel", "commentList: ${_commentList.value}")
        } catch (e: Exception) {
            _errorMessage.value = e.message
        } finally {
            _isLoading.value = false
        }
    }
}