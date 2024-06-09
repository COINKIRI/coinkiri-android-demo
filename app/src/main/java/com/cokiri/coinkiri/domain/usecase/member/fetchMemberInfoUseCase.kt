package com.cokiri.coinkiri.domain.usecase.member

import com.cokiri.coinkiri.data.local.entity.MemberInfoEntity
import com.cokiri.coinkiri.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 사용자 정보를 가져오는 UseCase
 */
class FetchMemberInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke() : Result<MemberInfoEntity?> {
        return runCatching { userRepository.getMemberInfo() }
    }
}