package com.ragnarok.auth.auth.request

import com.ragnarok.auth.auth.exception.NotEnoughIdentificationProvidedException
import com.ragnarok.auth.auth.usecase.MemberLoginRequest
import com.ragnarok.auth.member.domain.value.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class LoginRequest(
    private val id: Long?,
    private val phoneNumber: String?,
    private val nickName: String?,
    private val email: String?,
    @field:NotBlank
    @field:Size(min = 3, max = 15)
    private val password: String,
) {
    fun toMemberLoginRequest(): MemberLoginRequest {
        if (id == null && phoneNumber == null && nickName == null && email == null) {
            throw NotEnoughIdentificationProvidedException()
        }

        return MemberLoginRequest(
            id,
            phoneNumber,
            email?.let { Email(it) },
            nickName,
            password
        )
    }
}
