package com.ragnarok.auth.member.infra.repository

import com.ragnarok.auth.member.domain.entity.Member
import com.ragnarok.auth.member.domain.repository.MemberRepository
import com.ragnarok.auth.member.infra.entity.MemberJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
class JpaMemberRepository(private val crudRepository: JpaMemberCrudRepository) : MemberRepository {
    override fun findByPhoneNumber(phoneNumber: String): Member? {
        return crudRepository.findByPhoneNumber(phoneNumber)
            ?.toDomainEntity()
    }

    override fun findByNickName(nickName: String): Member? {
        return crudRepository.findByNickName(nickName)
            ?.toDomainEntity()
    }

    override fun findByEmail(email: String): Member? {
        return crudRepository.findByEmailAddress(email)
            ?.toDomainEntity()
    }

    override fun save(member: Member): Member {
        return crudRepository.save(MemberJpaEntity(member))
            .toDomainEntity()
    }

    override fun findById(id: Long): Member? {
        return crudRepository.findByIdOrNull(id)
            ?.toDomainEntity()
    }
}

@Component
interface JpaMemberCrudRepository : JpaRepository<MemberJpaEntity, Long> {
    fun findByPhoneNumber(phoneNumber: String): MemberJpaEntity?
    fun findByNickName(nickName: String): MemberJpaEntity?
    fun findByEmailAddress(email: String): MemberJpaEntity?
}
