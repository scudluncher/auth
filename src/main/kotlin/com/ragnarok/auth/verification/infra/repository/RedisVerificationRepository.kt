package com.ragnarok.auth.verification.infra.repository

import com.ragnarok.auth.verification.domain.entity.Verification
import com.ragnarok.auth.verification.domain.repository.VerificationRepository
import com.ragnarok.auth.verification.domain.value.VerificationType
import com.ragnarok.auth.verification.infra.entity.VerificationRedisEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
class RedisVerificationRepository(private val crudRepository: RedisVerificationCrudRepository) :
    VerificationRepository {
    override fun save(verification: Verification): Verification {
        return crudRepository.save(VerificationRedisEntity(verification))
            .toDomainEntity()
    }

    override fun findByPhoneNumberAndType(phoneNumber: String, type: VerificationType): Verification? {
        return crudRepository.findByPhoneNumberAndType(phoneNumber, type)
            ?.toDomainEntity()
    }
}

@Component
interface RedisVerificationCrudRepository : CrudRepository<VerificationRedisEntity, Long> {
    fun findByPhoneNumberAndType(phoneNumber: String, type: VerificationType): VerificationRedisEntity?
}
