package com.ragnarok.auth.verification.domain.entity

import com.ragnarok.auth.verification.domain.value.VerificationType
import com.ragnarok.auth.verification.usecase.value.TimeLimit
import java.time.LocalDateTime

class Verification(
    val id: Long?,
    val phoneNumber: String,
    val code: String,
    val codeExpiredTime: LocalDateTime,
    val type: VerificationType,
    val verificationExpiredTime: LocalDateTime? = null,
    val verified: Boolean = false,
) {
    private fun copy(
        id: Long? = this.id,
        phoneNumber: String = this.phoneNumber,
        code: String = this.code,
        codeExpiredTime: LocalDateTime = this.codeExpiredTime,
        type: VerificationType = this.type,
        verificationExpiredTime: LocalDateTime? = this.verificationExpiredTime,
        verified: Boolean = this.verified,
    ) = Verification(
        id,
        phoneNumber,
        code,
        codeExpiredTime,
        type,
        verificationExpiredTime,
        verified
    )

    fun verified(): Verification {
        return this.copy(
            verified = true,
            verificationExpiredTime = LocalDateTime.now().plusMinutes(TimeLimit.VERIFICATION_EXPIRED)
        )
    }
}
