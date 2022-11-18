package com.ragnarok.auth.member.domain.repository

import com.ragnarok.auth.member.domain.entity.Member

interface MemberRepository {
    fun findByPhoneNumber(phoneNumber: String): Member?
    fun findByNickName(nickName: String): Member?
    fun findByEmail(email: String): Member?
    fun save(member: Member): Member
    fun findById(id: Long): Member?
}

class FakeMemberRepository : MemberRepository {
    override fun findByPhoneNumber(phoneNumber: String): Member? {
        TODO("Not yet implemented")
    }

    override fun findByNickName(nickName: String): Member? {
        TODO("Not yet implemented")
    }

    override fun findByEmail(email: String): Member? {
        TODO("Not yet implemented")
    }

    override fun save(member: Member): Member {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Member? {
        TODO("Not yet implemented")
    }
}
