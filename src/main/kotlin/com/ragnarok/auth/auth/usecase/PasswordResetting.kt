package com.ragnarok.auth.auth.usecase

import com.ragnarok.auth.common.support.HashingProvider
import com.ragnarok.auth.member.domain.entity.Member
import com.ragnarok.auth.member.domain.repository.MemberRepository
import com.ragnarok.auth.member.domain.value.HashedPassword
import com.ragnarok.auth.member.exception.NoMemberFoundException
import com.ragnarok.auth.verification.domain.entity.checkUsableVerification
import com.ragnarok.auth.verification.domain.repository.VerificationRepository
import com.ragnarok.auth.verification.domain.value.VerificationType

class PasswordResetting(
    private val request: ResettingRequest,
    private val memberRepository: MemberRepository,
    private val verificationRepository: VerificationRepository,
    private val hashingProvider: HashingProvider,
) {
    fun execute(): Member {
        val verification = verificationRepository.findByPhoneNumberAndType(request.phoneNumber, VerificationType.RESET)
        verification.checkUsableVerification()

        val member = memberRepository.findByPhoneNumber(request.phoneNumber) ?: throw NoMemberFoundException()

        val salt = hashingProvider.salt()
        val hashedNewPassword = HashedPassword(
            hashingProvider.hash(request.newPassword, salt),
            salt
        )

        val updatedMember = member.changePassword(hashedNewPassword)

        return memberRepository.save(updatedMember)
    }
}

class ResettingRequest(
    val phoneNumber: String,
    val newPassword: String,
)
