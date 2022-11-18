package com.ragnarok.auth.member.controller

import com.ragnarok.auth.member.request.JoinRequest
import com.ragnarok.auth.member.service.MemberService
import com.ragnarok.auth.member.viewmodel.AuthResult
import com.ragnarok.auth.member.viewmodel.MyInformation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController("/members")
class MemberController(
    private val memberService: MemberService,
) {
    @GetMapping("/me")
    fun myInformation(): ResponseEntity<MyInformation> {
        TODO()
    }

    @PostMapping
    fun join(@Valid request:JoinRequest): ResponseEntity<AuthResult> {
        val member = memberService.join(request.toJoinRequest())
     TODO()
    }

    @PatchMapping
    fun resetPassword(): ResponseEntity<MyInformation> {
        TODO()
    }

}
