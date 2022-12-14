package com.ragnarok.auth.member.domain.repository

import com.ragnarok.auth.member.domain.entity.Member
import com.ragnarok.auth.member.domain.value.Email
import com.ragnarok.auth.member.domain.value.HashedPassword

interface MemberRepository {
    fun findByPhoneNumber(phoneNumber: String): Member?
    fun findByNickName(nickName: String): Member?
    fun findByEmail(email: Email): Member?
    fun save(member: Member): Member
    fun findById(id: Long): Member?
}

class FakeMemberRepository : MemberRepository {
    override fun findByPhoneNumber(phoneNumber: String): Member? {
        return members.firstOrNull { it.phoneNumber == phoneNumber }
    }

    override fun findByNickName(nickName: String): Member? {
        return members.firstOrNull { it.nickName == nickName }
    }

    override fun findByEmail(email: Email): Member? {
        return members.firstOrNull { it.email.address == email.address }
    }

    override fun save(member: Member): Member {
        if (member.id == 0L) {
            val newMember = Member(
                (members.maxOfOrNull { it.id } ?: 0) + 1,
                member.email,
                member.password,
                member.name,
                member.nickName,
                member.phoneNumber
            )
            members.add(newMember)

            return newMember
        } else {
            members.removeIf { it.id == member.id }
            members.add(member)

            return member
        }
    }

    override fun findById(id: Long): Member? {
        return members.firstOrNull { it.id == id }
    }

    private val members: MutableList<Member> = mutableListOf(
        Member(
            id = 1,
            email = Email("kratos@olympus.com"),
            password = HashedPassword("asdfqwer", "asdf"),
            name = "kratos",
            nickName = "Son Of Zeus",
            phoneNumber = "01011111111"
        ),
        Member(
            id = 2,
            email = Email("atreus@northern.com"),
            password = HashedPassword("qqwerwer", "zxcve"),
            name = "atreus",
            nickName = "Loki, Ragnarok Initiator",
            phoneNumber = "01022222222"
        ),
        Member(
            id = 3,
            email = Email("mimir@topofworld.com"),
            password = HashedPassword("gnhdfklewiodi", "euifjhvh"),
            name = "mimir",
            nickName = "wisdom king",
            phoneNumber = "01033333333"
        ),
        Member(
            id = 4,
            email = Email("freya@oldwitch.com"),
            password = HashedPassword("fjfdjheuyeui", "ckmdjweu"),
            name = "freya",
            nickName = "forest queen",
            phoneNumber = "01044444444"
        ),
        Member(
            id = 5,
            email = Email("thor@godof.power"),
            password = HashedPassword("fasdfqwer", "asdfwer"),
            name = "thor odinson",
            nickName = "god of thunder",
            phoneNumber = "01033334444"
        )
    )
}
