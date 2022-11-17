package com.ragnarok.auth.verification.service

import com.ragnarok.auth.member.domain.repository.MemberRepository
import com.ragnarok.auth.verification.domain.repository.VerificationRepository
import com.ragnarok.auth.verification.usecase.JoinVerificationGenerating
import com.ragnarok.auth.verification.usecase.VerificationRequest
import org.springframework.stereotype.Service

@Service
class VerificationService(
    private val verificationRepository: VerificationRepository,
    private val memberRepository: MemberRepository,
) {
    fun generateVerification(verificationRequest: VerificationRequest) {
        JoinVerificationGenerating(
            verificationRequest,
            verificationRepository,
            memberRepository
        )
            .execute()
    }
}
