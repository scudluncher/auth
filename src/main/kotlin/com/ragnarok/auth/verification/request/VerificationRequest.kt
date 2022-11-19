package com.ragnarok.auth.verification.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.ragnarok.auth.verification.usecase.VerificationRequest
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

class VerificationRequest(
    @field:NotBlank
    @field:JsonProperty
    @field:Pattern(regexp = "^[0-9]{11}", message ="ex) 01012349876 로 보내주세요" )
    private val phoneNumber: String
) {
    fun toGenerateRequest(): VerificationRequest {
        return VerificationRequest(phoneNumber)
    }
}
