package com.ragnarok.auth.member.service

import com.ragnarok.auth.common.support.HashingProvider
import com.ragnarok.auth.member.domain.entity.Member
import com.ragnarok.auth.member.domain.repository.MemberRepository
import com.ragnarok.auth.member.usecase.JoinRequest
import com.ragnarok.auth.member.usecase.MemberJoining
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val hashingProvider: HashingProvider,
) {
    fun join(request: JoinRequest): Member {
        return MemberJoining(
            request,
            memberRepository,
            hashingProvider
        )
            .execute()
    }

    fun me(): Member {
        TODO()
    }
}
