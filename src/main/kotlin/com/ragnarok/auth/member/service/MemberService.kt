package com.ragnarok.auth.member.service

import com.ragnarok.auth.auth.usecase.PasswordResetting
import com.ragnarok.auth.auth.usecase.ResettingRequest
import com.ragnarok.auth.common.support.HashingProvider
import com.ragnarok.auth.member.domain.entity.Member
import com.ragnarok.auth.member.domain.repository.MemberRepository
import com.ragnarok.auth.member.usecase.JoinRequest
import com.ragnarok.auth.member.usecase.MemberJoining
import com.ragnarok.auth.member.usecase.ShowingMe
import com.ragnarok.auth.verification.domain.repository.VerificationRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val verificationRepository: VerificationRepository,
    private val hashingProvider: HashingProvider,
) {
    fun join(request: JoinRequest): Member {
        return MemberJoining(
            request,
            memberRepository,
            verificationRepository,
            hashingProvider
        )
            .execute()
    }

    fun me(id: Long): Member {
        return ShowingMe(
            memberRepository,
            id
        )
            .retrieve()
    }

    fun resetPassword(request:ResettingRequest):Member {
        return PasswordResetting(
            request,
            memberRepository,
            verificationRepository,
            hashingProvider
        )
            .execute()
    }
}
