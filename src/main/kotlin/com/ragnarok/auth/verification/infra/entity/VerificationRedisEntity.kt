package com.ragnarok.auth.verification.infra.entity

import com.ragnarok.auth.verification.domain.entity.Verification
import com.ragnarok.auth.verification.domain.value.VerificationType
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime
import javax.persistence.EnumType
import javax.persistence.Enumerated

@RedisHash(value = "verification", timeToLive = 60 * 15)
class VerificationRedisEntity(
    @Id
    var id: Long?,

    @Indexed
    var phoneNumber: String,

    var code: String,

    var codeExpiredTime: LocalDateTime,

    @Enumerated(EnumType.STRING)
    var type: VerificationType,

    var verificationExpiredTime: LocalDateTime?,

    var verified: Boolean,
) {
    constructor(verification: Verification) : this(
        verification.id,
        verification.phoneNumber,
        verification.code,
        verification.codeExpiredTime,
        verification.type,
        verification.verificationExpiredTime,
        verification.verified
    )

    fun toDomainEntity(): Verification {
        return Verification(
            id,
            phoneNumber,
            code,
            codeExpiredTime,
            type,
            verificationExpiredTime,
            verified
        )
    }
}
