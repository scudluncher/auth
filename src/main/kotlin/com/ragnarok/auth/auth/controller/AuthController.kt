package com.ragnarok.auth.auth.controller

import com.ragnarok.auth.auth.request.PasswordResetRequest
import com.ragnarok.auth.common.extension.AuthenticationExtension
import com.ragnarok.auth.common.response.EmptyContent
import com.ragnarok.auth.common.response.SingleResponse
import com.ragnarok.auth.member.service.MemberService
import com.ragnarok.auth.member.viewmodel.AuthResult
import com.ragnarok.auth.member.viewmodel.MyInformation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AuthController(
    private val memberService: MemberService,
) : AuthenticationExtension {
    @PatchMapping("/v1/auth/reset")
    fun resetPassword(@Valid request: PasswordResetRequest): ResponseEntity<SingleResponse<MyInformation>> {
        val member = memberService.resetPassword(request.toResettingRequest())

        return ResponseEntity(SingleResponse.Ok(MyInformation(member)),HttpStatus.ACCEPTED)
    }

    @PostMapping("/v1/auth/login")
    fun login(): ResponseEntity<SingleResponse<AuthResult>> {
        TODO()
    }
}
