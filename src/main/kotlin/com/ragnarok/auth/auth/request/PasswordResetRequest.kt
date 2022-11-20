package com.ragnarok.auth.auth.request

import com.ragnarok.auth.auth.usecase.ResettingRequest
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class PasswordResetRequest(
    @field:NotBlank
    @field:Size(max = 15)
    @field:Pattern(regexp = "^[0-9]{11}", message = "ex) 01012349876 로 보내주세요")
    private val phoneNumber: String,
    @field:NotBlank
    @field:Size(min = 3, max = 15)
    private val newPassword: String,
) {
    fun toResettingRequest(): ResettingRequest {
        return ResettingRequest(
            phoneNumber,
            newPassword
        )
    }
}
