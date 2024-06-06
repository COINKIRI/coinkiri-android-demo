package com.cokiri.coinkiri.presentation.comment

import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.CommentList
import com.cokiri.coinkiri.data.remote.model.CommentRequest
import com.cokiri.coinkiri.domain.usecase.comment.AddCommentUseCase
import com.cokiri.coinkiri.domain.usecase.comment.FetchAllCommentsUseCase
import com.cokiri.coinkiri.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val fetchAllCommentsUseCase: FetchAllCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase
) : BaseViewModel() {

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
        executeWithLoading(
            block = {
                val content = _commentContent.value
                if (content.isBlank()) {
                    throw IllegalArgumentException("댓글 내용을 입력해주세요.")
                }

                val commentRequest = CommentRequest(postId, content)
                addCommentUseCase(commentRequest)
            },
            onSuccess = { result ->
                _submitCommentResult.value = Result.success(result)
                fetchCommentList(postId)
            },
            onFailure = { error ->
                _submitCommentResult.value = Result.failure(error)
            }
        )
    }


    /**
     * 댓글 목록을 가져오는 함수
     * @param postId 댓글 목록을 가져올 게시글의 ID
     */
    fun fetchCommentList(postId: Long) {
        executeWithLoading(
            block = { fetchAllCommentsUseCase(postId) },
            onSuccess = { commentList ->
                _commentList.value = commentList
            },
            onFailure = { error ->
                _commentList.value = emptyList()
            }
        )
    }


    /**
     * 댓글 내용이 변경될 때 호출되는 함수
     */
    fun onCommentContentChange(newCommentContent: String) {
        _commentContent.value = newCommentContent
    }
}