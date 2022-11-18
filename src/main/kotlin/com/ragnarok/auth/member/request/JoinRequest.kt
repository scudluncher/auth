package com.ragnarok.auth.member.request

import com.ragnarok.auth.member.usecase.JoinRequest
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class JoinRequest(
    @field:NotBlank
    @field:Size(max=50)
    private val email: String,
    //todo password validator
    private val password: String,
    @field:NotBlank
    @field:Size(max=30)
    private val name: String,
    @field:NotBlank
    @field:Size(max=30)
    private val nickName: String,
    @field:NotBlank
    @field:Size(max=15)
    private val phoneNumber: String,
) {
    fun toJoinRequest(): JoinRequest {
        return JoinRequest(
            email,
            password,
            name,
            nickName,
            phoneNumber
        )
    }
}
