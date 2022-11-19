package com.ragnarok.auth.auth.usecase

import com.ragnarok.auth.auth.exception.AuthenticationFailException
import com.ragnarok.auth.common.exception.BadRequestException
import com.ragnarok.auth.common.support.HashingProvider
import com.ragnarok.auth.member.domain.entity.Member
import com.ragnarok.auth.member.domain.repository.MemberRepository
import com.ragnarok.auth.member.domain.value.Email
import com.ragnarok.auth.member.exception.NoMemberFoundException

class MemberLogin(
    private val request: MemberLoginRequest,
    private val memberRepository: MemberRepository,
    private val hashingProvider: HashingProvider,
) {
    fun execute(): Member {
        val member = setOf(
            request.id?.let {
                memberRepository.findById(it)
            },
            request.phoneNumber?.let {
                memberRepository.findByPhoneNumber(it)
            },
            request.email?.let {
                memberRepository.findByEmail(it)
            },
            request.nickName?.let {
                memberRepository.findByNickName(it)
            }
        )
            .mapNotNull { it }
            .toSet()

        if (member.isEmpty()) {
            throw NoMemberFoundException()
        }

        if (member.size > 1) {
            throw BadRequestException()
        }

        val validMember = member.first()
        val salt = validMember.password.salt
        val currentPassword = validMember.password

        val requestedPasswordHashed = hashingProvider.hash(request.password, salt)

        if(!currentPassword.checkEquality(requestedPasswordHashed)) {
            throw AuthenticationFailException()
        }

        return validMember
    }
}

class MemberLoginRequest(
    val id: Long? = null,
    val phoneNumber: String? = null,
    val email: Email? = null,
    val nickName: String? = null,
    val password: String,
)
