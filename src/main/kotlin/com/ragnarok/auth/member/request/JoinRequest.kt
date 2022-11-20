package com.ragnarok.auth.member.request

import com.ragnarok.auth.member.usecase.JoinRequest
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class JoinRequest(
    @field:NotBlank
    @field:Size(max = 50)
    @field:Email
    private val email: String,

    @field:Size(min = 3, max = 15)
    private val password: String,

    @field:NotBlank
    @field:Size(max = 30)
    private val name: String,

    @field:NotBlank
    @field:Size(max = 30)
    private val nickName: String,

    @field:NotBlank
    @field:Size(max = 15)
    @field:Pattern(regexp = "^[0-9]{11}", message = "ex) 01012349876 로 보내주세요")
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
