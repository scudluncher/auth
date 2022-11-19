package com.ragnarok.auth.member.viewmodel

import com.ragnarok.auth.member.domain.entity.Member

class MyInformation(
    val id: Long,
    val email: String,
    val name: String,
    val nickName: String,
    val phoneNumber: String,
) {
    constructor(member: Member) : this(
        member.id,
        member.email.address,
        member.name,
        member.nickName,
        member.phoneNumber
    )
}
