package com.ragnarok.auth.auth.request

class PasswordResetRequest(
    private val phoneNumber: String,
    private val newPassword: String,
) {
}
