package com.ragnarok.auth.verification.domain.entity

import com.ragnarok.auth.verification.domain.value.VerificationType
import java.time.LocalDateTime

class Verification(
    val id: Long?,
    val phoneNumber: String,
    val code: String,
    val codeExpiredTime: LocalDateTime,
    val type: VerificationType,
    val verificationExpiredTime: LocalDateTime? = null,
    val verified: Boolean = false,
)
