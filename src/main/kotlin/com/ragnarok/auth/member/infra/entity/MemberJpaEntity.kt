package com.ragnarok.auth.member.infra.entity

import com.ragnarok.auth.common.infra.InfraEntity
import com.ragnarok.auth.member.domain.entity.Member
import com.ragnarok.auth.member.domain.value.Email
import com.ragnarok.auth.member.domain.value.HashedPassword
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "member")
class MemberJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var emailAddress: String,
    var password: String = "",
    var salt: String = "",
    var name: String,
    var nickName: String,
    var phoneNumber: String,
) : InfraEntity<Member> {
    constructor(member: Member) : this(
        member.id,
        member.email.address,
        member.password?.hashed ?: "",
        member.password?.salt ?: "",
        member.name,
        member.nickName,
        member.phoneNumber
    )

    override fun update(domainEntity: Member) {
        id = domainEntity.id
        emailAddress = domainEntity.email.address
        domainEntity.password?.let {
            password = it.hashed
            salt = it.salt
        }
        name = domainEntity.name
        nickName = domainEntity.nickName
        phoneNumber = domainEntity.phoneNumber
    }

    override fun toDomainEntity(): Member {
        return Member(
            id,
            Email(emailAddress),
            if (password.isNotBlank() && salt.isNotBlank()) HashedPassword(password, salt) else null,
            name,
            nickName,
            phoneNumber
        )
    }
}
