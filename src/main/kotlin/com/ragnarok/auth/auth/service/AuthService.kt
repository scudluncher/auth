package com.ragnarok.auth.auth.service

import com.ragnarok.auth.auth.usecase.MemberLogin
import com.ragnarok.auth.auth.usecase.MemberLoginRequest
import com.ragnarok.auth.common.support.HashingProvider
import com.ragnarok.auth.member.domain.entity.Member
import com.ragnarok.auth.member.domain.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val hashingProvider: HashingProvider,
) {
    fun login(request: MemberLoginRequest): Member {
        return MemberLogin(
            request,
            memberRepository,
            hashingProvider
        )
            .execute()
    }
}
