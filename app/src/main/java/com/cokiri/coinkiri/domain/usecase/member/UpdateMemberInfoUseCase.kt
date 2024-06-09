package com.cokiri.coinkiri.domain.usecase.member

import com.cokiri.coinkiri.data.remote.model.ApiResponse
import com.cokiri.coinkiri.data.remote.model.auth.UpdateMemberInfoRequest
import com.cokiri.coinkiri.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 사용자 정보를 업데이트하는 UseCase
 */
class UpdateMemberInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(updateMemberInfoRequest: UpdateMemberInfoRequest) : Result<ApiResponse> {
        return runCatching { userRepository.updateMemberInfo(updateMemberInfoRequest) }
    }
}