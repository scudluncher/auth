package com.ragnarok.auth.verification.domain.repository

import com.ragnarok.auth.verification.domain.entity.Verification
import com.ragnarok.auth.verification.domain.value.VerificationType

interface VerificationRepository {
    fun save(verification: Verification): Verification
    fun findByPhoneNumberAndType(phoneNumber: String, type: VerificationType): Verification?
}

class FakeVerificationRepository : VerificationRepository {
    override fun save(verification: Verification): Verification {
        TODO("Not yet implemented")
    }

    override fun findByPhoneNumberAndType(phoneNumber: String, type: VerificationType): Verification? {
        TODO("Not yet implemented")
    }

}
