package com.ragnarok.auth.auth.request

import com.ragnarok.auth.auth.usecase.ResettingRequest
import javax.validation.constraints.NotBlank

class PasswordResetRequest(
    @field:NotBlank
    private val phoneNumber: String,
    // TODO password validation annotation
    private val newPassword: String,
) {
    fun toResettingRequest(): ResettingRequest {
        return ResettingRequest(
            phoneNumber,
            newPassword
        )
    }
}
