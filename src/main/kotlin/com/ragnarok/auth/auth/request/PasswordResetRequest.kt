package com.ragnarok.auth.auth.request

import com.ragnarok.auth.auth.usecase.ResettingRequest
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class PasswordResetRequest(
    @field:NotBlank
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
