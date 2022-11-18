package com.ragnarok.auth.auth.usecase

class PasswordResetting {
}

class PasswordResetRequest(
    private val phoneNumber: String,
    private val newPassword: String,
)
