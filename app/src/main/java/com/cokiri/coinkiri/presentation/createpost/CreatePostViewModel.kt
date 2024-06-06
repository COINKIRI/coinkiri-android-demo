package com.cokiri.coinkiri.presentation.createpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.data.remote.model.AnalysisPostDataRequest
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.ImageData
import com.cokiri.coinkiri.data.remote.model.PostRequestDto
import com.cokiri.coinkiri.domain.usecase.analysis.SubmitAnalysisPostUseCase
import com.cokiri.coinkiri.domain.usecase.post.SubmitPostUseCase
import com.cokiri.coinkiri.extensions.executeWithLoading
import com.cokiri.coinkiri.util.InvestmentOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val submitPostUseCase: SubmitPostUseCase,
    private val submitAnalysisPostUseCase: SubmitAnalysisPostUseCase
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


    private val _coinId = MutableStateFlow(0L)
    val coinId: StateFlow<Long> get() = _coinId

    private val _coinPrevClosingPrice = MutableStateFlow("")
    val coinPrevClosingPrice: StateFlow<String> get() = _coinPrevClosingPrice

    private val _investmentOption = MutableStateFlow("")
    val investmentOption: StateFlow<String> get() = _investmentOption

    private val _targetPrice = MutableStateFlow("")
    val targetPrice: StateFlow<String> get() = _targetPrice

    private val _targetPeriod = MutableStateFlow("")
    val targetPeriod: StateFlow<String> get() = _targetPeriod

    /**
     * 분석글 작성시 분석 데이터를 설정하는 함수
     */
    fun setAnalysisData(
        coinId: Long,
        coinPrevClosingPrice: String,
        investmentOption: String,
        targetPrice: String,
        targetPeriod: String
    ) {
        _coinId.value = coinId
        _coinPrevClosingPrice.value = coinPrevClosingPrice
        _investmentOption.value = investmentOption
        _targetPrice.value = targetPrice
        _targetPeriod.value = targetPeriod
    }

    /**
     * 분석글 내용을 서버에 제출하는 함수
     */
    fun submitAnalysisPostContent() {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                val englishInvestmentOption = InvestmentOption.toEnglish(_investmentOption.value)

                val analysisPostDataRequest = AnalysisPostDataRequest(
                    postRequestDto = PostRequestDto(
                        title = _title.value,
                        content = _content.value,
                        images = _images.value.map { ImageData(it.first, it.second) }
                    ),
                    coinId = _coinId.value,
                    coinPrevClosingPrice = _coinPrevClosingPrice.value,
                    investmentOption = englishInvestmentOption,
                    targetPrice = _targetPrice.value,
                    targetPeriod = _targetPeriod.value
                )
                val result = submitAnalysisPostUseCase(analysisPostDataRequest)
                _submitResult.value = result
                result
            }
        }
    }


    /**
     * 게시글 내용을 서버에 제출하는 함수
     */
    fun submitPostContent() {
        viewModelScope.launch {
            executeWithLoading(_isLoading, _errorMessage) {
                val postRequestDto = PostRequestDto(
                    title = _title.value,
                    content = _content.value,
                    images = _images.value.map { ImageData(it.first, it.second) }
                )
                val result = submitPostUseCase(postRequestDto)
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
