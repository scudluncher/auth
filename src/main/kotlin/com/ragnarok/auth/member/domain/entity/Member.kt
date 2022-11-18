package com.ragnarok.auth.member.domain.entity

import com.ragnarok.auth.member.domain.value.Email
import com.ragnarok.auth.member.domain.value.HashedPassword

class Member(
    val id: Long = 0L,
    val email: Email,
    val password: HashedPassword?,
    val name: String,
    val nickName: String,
    val phoneNumber: String,
)
