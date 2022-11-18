package com.ragnarok.auth.member.controller

import com.ragnarok.auth.common.extension.AuthenticationExtension
import com.ragnarok.auth.common.response.SingleResponse
import com.ragnarok.auth.common.support.TokenPayload
import com.ragnarok.auth.common.support.TokenProvider
import com.ragnarok.auth.member.request.JoinRequest
import com.ragnarok.auth.member.service.MemberService
import com.ragnarok.auth.member.viewmodel.AuthResult
import com.ragnarok.auth.member.viewmodel.MyInformation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController("/v1/members")
class MemberController(
    private val memberService: MemberService,
    private val tokenProvider: TokenProvider,
) : AuthenticationExtension {
    @GetMapping("/me")
    fun myInformation(): ResponseEntity<SingleResponse<MyInformation>> {
        val myInfo = MyInformation(memberService.me(id()))

        return ResponseEntity.ok()
            .body(SingleResponse.Ok(myInfo))
    }

    @PostMapping
    fun join(@Valid request: JoinRequest): ResponseEntity<SingleResponse<AuthResult>> {
        val member = memberService.join(request.toJoinRequest())
        val token = tokenProvider.token(
            TokenPayload(member.id, member.nickName)
        )

        return ResponseEntity.ok()
            .body(SingleResponse.Ok(AuthResult(token)))
    }
}
