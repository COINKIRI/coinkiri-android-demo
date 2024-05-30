package com.cokiri.coinkiri.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommentList
import com.cokiri.coinkiri.data.remote.model.CommentRequest
import com.cokiri.coinkiri.data.remote.model.CommunityDetailResponseDto
import com.cokiri.coinkiri.data.remote.model.CommunityResponseDto
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

    // 커뮤니티 게시글 목록을 관리하는 MutableStateFlow
    private val _communityPostList = MutableStateFlow<List<CommunityResponseDto>>(emptyList())
    // 외부에서 읽기만 가능한 커뮤니티 게시글 목록 StateFlow
    val communityPostList: StateFlow<List<CommunityResponseDto>> = _communityPostList

    // 커뮤니티 게시글 상세 정보를 관리하는 MutableStateFlow
    private val _communityDetail = MutableStateFlow<CommunityDetailResponseDto?>(null)
    // 외부에서 읽기만 가능한 커뮤니티 게시글 상세 정보 StateFlow
    val communityDetail: StateFlow<CommunityDetailResponseDto?> = _communityDetail

    // 로딩 상태를 관리하는 MutableStateFlow
    private val _isLoading = MutableStateFlow(false)
    // 외부에서 읽기만 가능한 로딩 상태 StateFlow
    val isLoading: StateFlow<Boolean> = _isLoading

    // 에러 메시지를 관리하는 MutableStateFlow
    private val _errorMessage = MutableStateFlow<String?>(null)
    // 외부에서 읽기만 가능한 에러 메시지 StateFlow
    val errorMessage: StateFlow<String?> = _errorMessage

    // 댓글 목록을 관리하는 MutableStateFlow
    private val _commentList = MutableStateFlow<List<CommentList>>(emptyList())
    // 외부에서 읽기만 가능한 댓글 목록 StateFlow
    val commentList: StateFlow<List<CommentList>> = _commentList

    // 작성 중인 댓글 내용을 관리하는 MutableStateFlow
    private val _commentContent = MutableStateFlow("")
    // 외부에서 읽기만 가능한 작성 중인 댓글 내용 StateFlow
    val commentContent: StateFlow<String> = _commentContent

    // 댓글 작성 결과를 관리하는 MutableStateFlow
    private val _submitCommentResult = MutableStateFlow<Result<ApiResponse>?>(null)



    // 댓글 내용이 변경될 때 호출되는 함수
    fun onCommentContentChange(newCommentContent: String) {
        _commentContent.value = newCommentContent
    }

    // 댓글을 작성하는 함수
    fun submitComment(postId: Long) {
        viewModelScope.launch {
            try {
                val content = _commentContent.value
                // 댓글 내용이 비어 있으면 에러 메시지 설정 후 종료
                if (content.isBlank()) {
                    _errorMessage.value = "댓글 내용을 입력해주세요."
                    return@launch
                }
                // 댓글 요청 객체 생성
                val commentRequest = CommentRequest(
                    postId = postId,
                    content = content
                )
                // 댓글 작성 결과를 UseCase를 통해 받아옴
                _submitCommentResult.value = submitCommentUseCase(commentRequest)
                // 댓글 작성 성공 시 댓글 목록 갱신, 실패 시 에러 메시지 설정
                _submitCommentResult.value?.let {
                    if (it.isSuccess) {
                        fetchCommentList(postId)
                    } else {
                        _errorMessage.value = "댓글 작성에 실패하였습니다."
                    }
                }
            } catch (e: Exception) {
                // 예외 발생 시 에러 메시지 설정
                _errorMessage.value = e.message
            }
        }
    }

    // 특정 게시글의 댓글 목록을 가져오는 함수
    suspend fun fetchCommentList(postId: Long) {
        executeWithLoading {
            _commentList.value = getCommentListUseCase(postId)
        }
    }

    // 커뮤니티 게시글 목록을 가져오는 함수
    fun fetchCommunityPostList() {
        viewModelScope.launch {
            executeWithLoading {
                _communityPostList.value = getCommunityPostListUseCase()
            }
        }
    }

    // 특정 게시글의 상세 정보를 가져오는 함수
    suspend fun fetchCommunityPostDetail(postId: Long) {
        viewModelScope.launch {
            executeWithLoading {
                _communityDetail.value = getCommunityPostDetailUseCase(postId)
            }
        }
    }

    // 로딩 상태를 관리하는 공통 함수
    private suspend fun <T> executeWithLoading(block: suspend () -> T) {
        _isLoading.value = true
        _errorMessage.value = null
        try {
            block()
        } catch (e: Exception) {
            // 예외 발생 시 에러 메시지 설정
            _errorMessage.value = e.message
        } finally {
            _isLoading.value = false
        }
    }
}
