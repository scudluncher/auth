package com.ragnarok.auth.verification.service

import com.ragnarok.auth.common.support.ExternalMessageSender
import com.ragnarok.auth.member.domain.repository.MemberRepository
import com.ragnarok.auth.verification.domain.repository.VerificationRepository
import com.ragnarok.auth.verification.usecase.*
import org.springframework.stereotype.Service

@Service
class VerificationService(
    private val verificationRepository: VerificationRepository,
    private val memberRepository: MemberRepository,
    private val messageSender: ExternalMessageSender,
) {
    fun generateJoinVerification(verificationRequest: VerificationRequest) {
        GeneratingJoinVerification(
            verificationRequest,
            verificationRepository,
            memberRepository,
            messageSender
        )
            .execute()
    }

    fun generateResetVerification(verificationRequest: VerificationRequest) {
        GeneratingResetVerification(
            verificationRequest,
            verificationRepository,
            memberRepository,
            messageSender
        )
            .execute()
    }

    fun confirmVerification(verificationConfirmRequest: VerificationConfirmRequest) {
        ConfirmingVerification(
            verificationConfirmRequest,
            verificationRepository
        )
            .execute()
    }
}
