package com.ragnarok.auth.member.domain.entity

import com.ragnarok.auth.member.domain.value.Email

class Member(
    val email: Email,
    val password: String,
    val name: String,
    val nickName: String,
    val phoneNumber: String
)
