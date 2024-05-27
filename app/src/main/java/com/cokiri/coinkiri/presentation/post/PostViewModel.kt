package com.cokiri.coinkiri.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.CommunityResponseDto
import com.cokiri.coinkiri.domain.usecase.GetCommunityPostListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getCommunityPostListUseCase: GetCommunityPostListUseCase
) : ViewModel() {

    private val _communityPostList = MutableStateFlow<List<CommunityResponseDto>>(emptyList())
    val communityPostList: StateFlow<List<CommunityResponseDto>> = _communityPostList

    private val _communityDetail = MutableStateFlow<CommunityResponseDto?>(null)
    val communityDetail: StateFlow<CommunityResponseDto?> = _communityDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

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

    fun fetchCommunityDetail(postId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _communityDetail.value = _communityPostList.value.find { it.postResponseDto.id.toString() == postId }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}