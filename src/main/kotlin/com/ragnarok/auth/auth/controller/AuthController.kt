package com.ragnarok.auth.auth.controller

import com.ragnarok.auth.auth.request.LoginRequest
import com.ragnarok.auth.auth.service.AuthService
import com.ragnarok.auth.common.extension.AuthenticationExtension
import com.ragnarok.auth.common.response.SingleResponse
import com.ragnarok.auth.common.support.TokenPayload
import com.ragnarok.auth.common.support.TokenProvider
import com.ragnarok.auth.member.viewmodel.AuthResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AuthController(
    private val authService: AuthService,
    private val tokenProvider: TokenProvider,
) : AuthenticationExtension {
    @PostMapping("/v1/auth/login")
    fun login(@Valid request: LoginRequest): ResponseEntity<SingleResponse<AuthResult>> {
        val member = authService.login(request.toMemberLoginRequest())
        val token = tokenProvider.token(
            TokenPayload(member.id, member.nickName)
        )

        return ResponseEntity.ok()
            .body(SingleResponse.Ok(AuthResult(token)))
    }
}
