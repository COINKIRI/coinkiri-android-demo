package com.cokiri.coinkiri.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.ImageData
import com.cokiri.coinkiri.data.remote.model.PostDataRequest
import com.cokiri.coinkiri.domain.usecase.SubmitPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityWriteViewModel @Inject constructor(
    private val submitPostUseCase: SubmitPostUseCase
) : ViewModel() {


    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content

    private val _images = MutableStateFlow<List<Pair<Int, String>>>(emptyList())

    private val _submitResult = MutableStateFlow<Result<ApiResponse>?>(null)

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onContentChange(newContent: String) {
        _content.value = newContent
    }

    fun onImagesChange(newImages: List<Pair<Int, String>>) {
        _images.value = newImages
    }


    // 게시글 등록 처리
    fun submitPost() {
        viewModelScope.launch {
            val postDataRequest = PostDataRequest(
                title = _title.value,
                content = _content.value,
                images = _images.value.map { ImageData(it.first, it.second) }
            )
            _submitResult.value = submitPostUseCase(postDataRequest)
        }
    }
}