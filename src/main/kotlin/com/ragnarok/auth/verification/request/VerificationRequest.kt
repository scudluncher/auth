package com.ragnarok.auth.verification.request

import com.ragnarok.auth.verification.usecase.VerificationRequest
import javax.validation.constraints.NotBlank

class VerificationRequest(
    @field:NotBlank
    private val phoneNumber: String,
) {
    fun toGenerateRequest(): VerificationRequest {
        return VerificationRequest(phoneNumber)
    }
}
