package com.cokiri.coinkiri.presentation.screens.profile

import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity
import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.auth.UpdateMemberInfoRequest
import com.cokiri.coinkiri.domain.usecase.member.FetchMemberInfoUseCase
import com.cokiri.coinkiri.domain.usecase.member.UpdateMemberInfoUseCase
import com.cokiri.coinkiri.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fetchMemberInfoUseCase: FetchMemberInfoUseCase,
    private val updateMemberInfoUseCase: UpdateMemberInfoUseCase
) : BaseViewModel() {

    // 사용자 정보를 관리하는 MutableStateFlow
    private val _memberInfo = MutableStateFlow<MemberInfoEntity?>(null)
    val memberInfo: StateFlow<MemberInfoEntity?> = _memberInfo

    // 사용자 닉네임을 관리하는 MutableStateFlow
    private val _nickName = MutableStateFlow("")
    val nickName: StateFlow<String> = _nickName

    // 사용자 프로필 이미지를 관리하는 MutableStateFlow
    private val _profileImage = MutableStateFlow("")
    val profileImage: StateFlow<String?> = _profileImage

    // 사용자 소개글을 관리하는 MutableStateFlow
    private val _statusMessage = MutableStateFlow("")
    val statusMessage: StateFlow<String> = _statusMessage

    // 초기 값을 저장하는 변수
    private var initialNickName: String = ""
    private var initialStatusMessage: String = ""

    // 사용자 정보 수정 결과를 관리하는 MutableStateFlow
    private val _submitResult = MutableStateFlow<Result<ApiResponse>?>(null)
    val submitResult: StateFlow<Result<ApiResponse>?> = _submitResult

    init {
        fetchMemberInfo()
    }

    fun fetchMemberInfo() {
        executeWithLoading(
            block = { fetchMemberInfoUseCase() },
            onSuccess = { memberInfo ->
                _memberInfo.value = memberInfo
                memberInfo?.let {
                    initialNickName = it.nickname
                    initialStatusMessage = it.statusMessage

                    _nickName.value = it.nickname
                    _statusMessage.value = it.statusMessage
                }
            }
        )
    }

    fun onNickNameChanged(newNickName: String) {
        _nickName.value = newNickName
    }

    fun onStatusMessageChanged(newStatusMessage: String) {
        _statusMessage.value = newStatusMessage
    }

    fun submitMemberInfo() {
        executeWithLoading(
            block = {
                val updateMemberInfoRequest = UpdateMemberInfoRequest(
                    nickname = _nickName.value.ifEmpty { initialNickName },
                    statusMessage = _statusMessage.value.ifEmpty { initialStatusMessage }
                )
                updateMemberInfoUseCase(updateMemberInfoRequest)
            },
            onSuccess = { result ->
                _submitResult.value = Result.success(result)
                fetchMemberInfo()
            }
        )
    }
}
