package com.cokiri.coinkiri.presentation.createpost

import com.cokiri.coinkiri.data.remote.model.analysis.AnalysisPostDataRequest
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.analysis.ImageData
import com.cokiri.coinkiri.data.remote.model.analysis.PostRequestDto
import com.cokiri.coinkiri.domain.usecase.analysis.SubmitAnalysisPostUseCase
import com.cokiri.coinkiri.domain.usecase.post.SubmitPostUseCase
import com.cokiri.coinkiri.util.InvestmentOption
import com.cokiri.coinkiri.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val submitPostUseCase: SubmitPostUseCase,
    private val submitAnalysisPostUseCase: SubmitAnalysisPostUseCase
) : BaseViewModel() {

    // 게시글 제목을 관리하는 MutableStateFlow
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    // 게시글 내용을 관리하는 MutableStateFlow
    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content

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
        executeWithLoading(
            block = {
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
                submitAnalysisPostUseCase(analysisPostDataRequest)
            },
            onSuccess = { result ->
                _submitResult.value = Result.success(result)
                clearForm()
            },
            onFailure = { error ->
                _submitResult.value = Result.failure(error)
            }
        )
    }


    /**
     * 게시글 내용을 서버에 제출하는 함수
     */
    fun submitPostContent() {
        executeWithLoading(
            block = {
                val postRequestDto = PostRequestDto(
                    title = _title.value,
                    content = _content.value,
                    images = _images.value.map { ImageData(it.first, it.second) }
                )
                submitPostUseCase(postRequestDto)
            },
            onSuccess = { result ->
                _submitResult.value = Result.success(result)
                clearForm()
            },
            onFailure = { error ->
                _submitResult.value = Result.failure(error)
            }
        )
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


    /**
     * 폼 초기화 함수
     */
    private fun clearForm() {
        _title.value = ""
        _content.value = ""
        _images.value = emptyList()
    }
}
