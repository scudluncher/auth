package com.ragnarok.auth.member.controller

import com.ragnarok.auth.auth.request.PasswordResetRequest
import com.ragnarok.auth.common.extension.AuthenticationExtension
import com.ragnarok.auth.common.response.SingleResponse
import com.ragnarok.auth.member.request.JoinRequest
import com.ragnarok.auth.member.service.MemberService
import com.ragnarok.auth.member.viewmodel.MyInformation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class MemberController(private val memberService: MemberService) : AuthenticationExtension {
    @GetMapping("/v1/members/me")
    fun myInformation(): ResponseEntity<SingleResponse<MyInformation>> {
        val myInfo = MyInformation(memberService.me(id()))

        return ResponseEntity.ok()
            .body(SingleResponse.Ok(myInfo))
    }

    @PostMapping("/v1/members")
    fun join(@RequestBody @Valid request: JoinRequest): ResponseEntity<SingleResponse<MyInformation>> {
        val member = memberService.join(request.toJoinRequest())

        return ResponseEntity.ok()
            .body(SingleResponse.Ok(MyInformation(member)))
    }

    @PatchMapping("/v1/members/password-reset")
    fun resetPassword(@RequestBody @Valid request: PasswordResetRequest): ResponseEntity<SingleResponse<MyInformation>> {
        val member = memberService.resetPassword(request.toResettingRequest())

        return ResponseEntity(SingleResponse.Ok(MyInformation(member)), HttpStatus.ACCEPTED)
    }
}
