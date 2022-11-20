package com.ragnarok.auth.verification.request

import com.ragnarok.auth.verification.usecase.VerificationRequest
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class VerificationRequest(
    @field:NotBlank
    @field:Size(max = 15)
    @field:Pattern(regexp = "^[0-9]{11}", message = "ex) 01012349876 로 보내주세요")
    private val phoneNumber: String,
) {
    fun toGenerateRequest(): VerificationRequest {
        return VerificationRequest(phoneNumber)
    }
}
