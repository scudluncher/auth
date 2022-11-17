package com.ragnarok.auth.member.domain.repository

import com.ragnarok.auth.member.domain.entity.Member

interface MemberRepository {
    fun findByPhoneNumber(phoneNumber: String): Member?
}

class FakeMemberRepository : MemberRepository {
    override fun findByPhoneNumber(phoneNumber: String): Member? {
        TODO("Not yet implemented")
    }
}
