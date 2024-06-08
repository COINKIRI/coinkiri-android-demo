package com.cokiri.coinkiri.viewmodel

import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.coin.WatchlistCoinPrice
import com.cokiri.coinkiri.domain.usecase.like.AddLikeUseCase
import com.cokiri.coinkiri.domain.usecase.like.CheckLikeUseCase
import com.cokiri.coinkiri.domain.usecase.like.DeleteLikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikeViewModel @Inject constructor(
    private val addLikeUseCase: AddLikeUseCase,
    private val checkLikeUseCase: CheckLikeUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase
) : BaseViewModel() {

    // 좋아요 목록에 게시글을 저장하는 StateFlow
    private val _likeList = MutableStateFlow<List<Long>>(emptyList())
    val likeList: StateFlow<List<Long>> get() = _likeList

    // 좋아요 여부를 저장하는 StateFlow
    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> get() = _isLiked

    /**
     * 좋아요를 누름
     */
    private fun addLike(postId: Long, callback: (Result<ApiResponse>) -> Unit) {
        viewModelScope.launch {
            addLikeUseCase.execute(postId) { result ->
                if (result.isSuccess) {
                    _likeList.value += postId
                    _isLiked.value = true
                }
                callback(result)
            }
        }
    }

    /**
     * 좋아요 여부 확인
     */
    fun checkLike(postId: Long, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            checkLikeUseCase.execute(postId) { result ->
                _isLiked.value = result
                callback(result)
            }
        }
    }

    /**
     * 좋아요를 취소함
     */
    private fun deleteLike(postId: Long, callback: (Result<ApiResponse>) -> Unit) {
        viewModelScope.launch {
            deleteLikeUseCase.execute(postId) { result ->
                if (result.isSuccess) {
                    _likeList.value -= postId
                    _isLiked.value = false
                }
                callback(result)
            }
        }
    }

    fun toggleLike(postId: Long) {
        viewModelScope.launch {
            checkLike(postId) { isLiked ->
                if (isLiked) {
                    deleteLike(postId) { result ->
                        if (result.isSuccess) {
                            _isLiked.value = false
                        }
                    }
                } else {
                    addLike(postId) { result ->
                        if (result.isSuccess) {
                            _isLiked.value = true
                        }
                    }
                }
            }
        }
    }
}
