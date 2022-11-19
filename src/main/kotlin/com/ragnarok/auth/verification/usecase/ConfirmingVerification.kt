package com.ragnarok.auth.verification.usecase

import com.ragnarok.auth.verification.domain.repository.VerificationRepository
import com.ragnarok.auth.verification.domain.value.VerificationType
import com.ragnarok.auth.verification.exception.CodeExpiredException
import com.ragnarok.auth.verification.exception.CodeNotMatchedException
import com.ragnarok.auth.verification.exception.NotExistingVerification
import java.time.LocalDateTime

class ConfirmingVerification(
    private val request: VerificationConfirmRequest,
    private val verificationRepository: VerificationRepository,
) {
    fun execute() {
        val verification = verificationRepository.findByPhoneNumberAndType(request.phoneNumber, request.type)
            ?: throw NotExistingVerification()

        if (verification.code != request.code) {
            throw CodeNotMatchedException()
        }

        if (verification.codeExpiredTime.isBefore(LocalDateTime.now())) {
            throw CodeExpiredException()
        }

        verificationRepository.save(verification.verified())
    }
}

class VerificationConfirmRequest(
    val phoneNumber: String,
    val code: String,
    val type: VerificationType,
)
