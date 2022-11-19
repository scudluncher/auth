package com.ragnarok.auth.verification.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.ragnarok.auth.verification.usecase.VerificationRequest
import javax.validation.constraints.NotBlank

class VerificationRequest(
    @field:NotBlank
    @field:JsonProperty
    private val phoneNumber: String
) {
    fun toGenerateRequest(): VerificationRequest {
        return VerificationRequest(phoneNumber)
    }
}
