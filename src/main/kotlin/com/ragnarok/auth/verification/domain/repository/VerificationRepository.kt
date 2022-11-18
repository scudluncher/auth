package com.ragnarok.auth.verification.domain.repository

import com.ragnarok.auth.verification.domain.entity.Verification
import com.ragnarok.auth.verification.domain.value.VerificationType
import com.ragnarok.auth.verification.usecase.value.TimeLimit
import java.time.LocalDateTime

interface VerificationRepository {
    fun save(verification: Verification): Verification
    fun findByPhoneNumberAndType(phoneNumber: String, type: VerificationType): Verification?
    fun delete(id: Long)
}

class FakeVerificationRepository : VerificationRepository {
    override fun save(verification: Verification): Verification {
        if (verification.id == null) {
            val newVerification = Verification(
                (verifications.maxOf { it.id ?: 0 }) + 1,
                verification.phoneNumber,
                verification.code,
                verification.codeExpiredTime,
                verification.type,
                verification.verificationExpiredTime,
                verification.verified
            )
            verifications.add(newVerification)

            return newVerification
        } else {
            verifications.removeIf { it.id == verification.id }
            verifications.add(verification)

            return verification
        }
    }

    override fun findByPhoneNumberAndType(phoneNumber: String, type: VerificationType): Verification? {
        return verifications.sortedByDescending { it.codeExpiredTime }
            .firstOrNull { it.phoneNumber == phoneNumber && it.type == type }
    }

    override fun delete(id: Long) {
        verifications.removeIf { it.id == id }
    }

    private val verifications: MutableList<Verification> = mutableListOf(
        // 최초 가입자 인증코드 미제출, 가입 불가 상태
        Verification(
            id = 1,
            phoneNumber = "01055555555",
            code = "555555",
            codeExpiredTime = LocalDateTime.now().plusMinutes(TimeLimit.CODE_EXPIRED),
            type = VerificationType.JOIN,
            verified = false,
        ),
        // 최초 가입자, 올바른 인증코드 제출 성공, 가입 가능 상태
        Verification(
            id = 1,
            phoneNumber = "01066666666",
            code = "444444",
            codeExpiredTime = LocalDateTime.now().plusMinutes(TimeLimit.CODE_EXPIRED),
            type = VerificationType.JOIN,
            verified = false,
        ),
        // cretos : 비밀번호 리셋 진행중, 인증코드 미제출 상태
        Verification(
            id = 3,
            phoneNumber = "01011111111",
            code = "999999",
            codeExpiredTime = LocalDateTime.now().plusMinutes(TimeLimit.CODE_EXPIRED),
            type = VerificationType.RESET,
            verified = false,
        ),
        // atreus : 비밀번호 리셋 진행 중, 올바른 인증코드 제출 성공
        Verification(
            id = 4,
            phoneNumber = "01022222222",
            code = "888888",
            codeExpiredTime = LocalDateTime.now(),
            type = VerificationType.RESET,
            verificationExpiredTime = LocalDateTime.now().plusMinutes(TimeLimit.VERIFICATION_EXPIRED),
            verified = true,
        ),
    )
}
