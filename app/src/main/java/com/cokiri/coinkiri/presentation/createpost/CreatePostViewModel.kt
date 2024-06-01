package com.cokiri.coinkiri.presentation.createpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.ImageData
import com.cokiri.coinkiri.data.remote.model.PostDataRequest
import com.cokiri.coinkiri.domain.usecase.SubmitPostContentUseCase
import com.cokiri.coinkiri.extensions.executeWithLoading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val submitPostContentUseCase: SubmitPostContentUseCase
) : ViewModel() {

    // 게시글 제목을 관리하는 MutableStateFlow
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    // 게시글 내용을 관리하는 MutableStateFlow
    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content

    // 로딩 상태를 관리하는 MutableStateFlow
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // 에러 메시지를 관리하는 MutableStateFlow
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // 이미지 목록을 관리하는 MutableStateFlow
    private val _images = MutableStateFlow<List<Pair<Int, String>>>(emptyList())

    // 게시글 등록 결과를 관리하는 MutableStateFlow
    private val _submitResult = MutableStateFlow<Result<ApiResponse>?>(null)
    val submitResult: StateFlow<Result<ApiResponse>?> = _submitResult


    /**
     * 게시글 내용을 서버에 제출하는 함수
     */
    fun submitPostContent() {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                val postDataRequest = PostDataRequest(
                    title = _title.value,
                    content = _content.value,
                    images = _images.value.map { ImageData(it.first, it.second) }
                )
                val result = submitPostContentUseCase(postDataRequest)
                _submitResult.value = result
                result
            }
        }
    }


    /**
     * 글 작성시 제목이 변경될 때 호출되는 함수
     */
    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }


    /**
     * 글 작성시 내용이 변경될 때 호출되는 함수
     */
    fun onContentChange(newContent: String) {
        _content.value = newContent
    }


    /**
     * 글 작성시 이미지가 변경될 때 호출되는 함수
     */
    fun onImagesChange(newImages: List<Pair<Int, String>>) {
        _images.value = newImages
    }
}
