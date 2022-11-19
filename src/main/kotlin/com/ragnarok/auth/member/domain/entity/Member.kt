package com.ragnarok.auth.member.domain.entity

import com.ragnarok.auth.member.domain.value.Email
import com.ragnarok.auth.member.domain.value.HashedPassword

class Member(
    val id: Long = 0L,
    val email: Email,
    val password: HashedPassword,
    val name: String,
    val nickName: String,
    val phoneNumber: String,
) {
    private fun copy(
        id: Long = this.id,
        email: Email = this.email,
        password: HashedPassword = this.password,
        name: String = this.name,
        nickName: String = this.nickName,
        phoneNumber: String = this.phoneNumber,
    ) = Member(
        id,
        email,
        password,
        name,
        nickName,
        phoneNumber
    )

    fun changePassword(newPassword: HashedPassword): Member {
        return copy(password = newPassword)
    }
}
