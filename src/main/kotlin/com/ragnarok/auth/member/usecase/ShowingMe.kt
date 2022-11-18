package com.ragnarok.auth.member.usecase

import com.ragnarok.auth.member.domain.repository.MemberRepository
import com.ragnarok.auth.member.exception.NoMemberFoundException

class ShowingMe(
    private val memberRepository: MemberRepository,
    private val id: Long,
) {
    fun retrieve() = memberRepository.findById(id) ?: throw NoMemberFoundException()
}
