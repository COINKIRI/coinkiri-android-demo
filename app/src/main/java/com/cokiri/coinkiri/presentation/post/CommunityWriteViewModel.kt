package com.cokiri.coinkiri.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityWriteViewModel @Inject constructor(

) : ViewModel(){

    private val _title = MutableStateFlow("")
    val title : StateFlow<String> get() = _title


    private val _content = MutableStateFlow("")
    val content : StateFlow<String> get() = _content

    fun onTitleChange(newTitle: String) {
        viewModelScope.launch {
            _title.emit(newTitle)
        }
    }

    fun onContentChange(newContent: String) {
        viewModelScope.launch {
            _content.emit(newContent)
        }
    }


    // 게시글 등록 처리
    fun submitPost() {
        val currentTitle = _title.value
        val currentContent = _content.value

        // 여기서 서버에 게시글을 등록하는 로직을 추가할 수 있습니다.
        // 예를 들어, 네트워크 요청을 통해 게시글 데이터를 서버로 전송합니다.
    }
}