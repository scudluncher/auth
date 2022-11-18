package com.ragnarok.auth.verification.request

import com.ragnarok.auth.common.exception.BadRequestException
import com.ragnarok.auth.verification.domain.value.VerificationType
import com.ragnarok.auth.verification.usecase.VerificationConfirmRequest
import javax.validation.constraints.NotBlank

class VerificationConfirmRequest(
    @field:NotBlank
    private val phoneNumber: String,
    @field:NotBlank
    private val code: String,
    @field:NotBlank
    private val verificationType: String,
) {
    fun toConfirmRequest(): VerificationConfirmRequest {
        return VerificationConfirmRequest(phoneNumber, code, type())
    }

    private fun type(): VerificationType {
        return if (verificationType.isNotBlank()) {
            try {
                VerificationType.valueOf(verificationType)
            } catch (e: IllegalArgumentException) {
                throw BadRequestException(e)
            }
        } else {
            throw BadRequestException()
        }
    }
}
