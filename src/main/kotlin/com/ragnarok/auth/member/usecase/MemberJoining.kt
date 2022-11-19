package com.ragnarok.auth.member.usecase

import com.ragnarok.auth.common.support.HashingProvider
import com.ragnarok.auth.member.domain.entity.Member
import com.ragnarok.auth.member.domain.repository.MemberRepository
import com.ragnarok.auth.member.domain.value.Email
import com.ragnarok.auth.member.domain.value.HashedPassword
import com.ragnarok.auth.member.exception.AlreadyUsedEmailException
import com.ragnarok.auth.member.exception.AlreadyUsedNickNameException
import com.ragnarok.auth.member.exception.AlreadyUsedPhoneNumberException
import com.ragnarok.auth.verification.domain.entity.checkUsableVerification
import com.ragnarok.auth.verification.domain.repository.VerificationRepository
import com.ragnarok.auth.verification.domain.value.VerificationType

class MemberJoining(
    private val request: JoinRequest,
    private val memberRepository: MemberRepository,
    private val verificationRepository: VerificationRepository,
    private val hashingProvider: HashingProvider,
) {
    fun execute(): Member {
        judgeKeyUsable(request)

        val verification = verificationRepository.findByPhoneNumberAndType(request.phoneNumber, VerificationType.JOIN)
        verification.checkUsableVerification()

        val salt = hashingProvider.salt()
        val hashedPassword = HashedPassword(
            hashingProvider.hash(request.password, salt),
            salt
        )

        return memberRepository.save(
            Member(
                email = Email(request.email),
                password = hashedPassword,
                name = request.name,
                nickName = request.nickName,
                phoneNumber = request.phoneNumber
            )
        )
    }

    private fun judgeKeyUsable(request: JoinRequest) {
        memberRepository.findByPhoneNumber(request.phoneNumber)
            ?.let {
                throw AlreadyUsedPhoneNumberException()
            }

        memberRepository.findByNickName(request.nickName)
            ?.let {
                throw AlreadyUsedNickNameException()
            }

        memberRepository.findByEmail(Email(request.email))
            ?.let {
                throw AlreadyUsedEmailException()
            }
    }
}

class JoinRequest(
    val email: String,
    val password: String,
    val name: String,
    val nickName: String,
    val phoneNumber: String,
)
