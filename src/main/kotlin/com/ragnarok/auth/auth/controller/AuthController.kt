package com.ragnarok.auth.auth.controller

import com.ragnarok.auth.common.extension.AuthenticationExtension
import com.ragnarok.auth.common.response.EmptyContent
import com.ragnarok.auth.common.response.SingleResponse
import com.ragnarok.auth.member.service.MemberService
import com.ragnarok.auth.member.viewmodel.AuthResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/v1/auth")
class AuthController(
    private val memberService: MemberService,
) : AuthenticationExtension {
    @PatchMapping("/reset")
    fun resetPassword(): ResponseEntity<SingleResponse<EmptyContent>> {
        TODO()
    }

    @PostMapping("/login")
    fun login(): ResponseEntity<SingleResponse<AuthResult>> {
        TODO()
    }
}
