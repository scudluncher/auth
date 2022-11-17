package com.ragnarok.auth.verification.request

import javax.validation.constraints.NotBlank

class VerificationConfirmRequest(
    @field:NotBlank
    private val phoneNumber: String,
    @field:NotBlank
    private val randomCode: String,
) {
}
