package com.ragnarok.auth.verification.controller

import com.ragnarok.auth.common.response.EmptyContent
import com.ragnarok.auth.common.response.SingleResponse
import com.ragnarok.auth.verification.request.VerificationConfirmRequest
import com.ragnarok.auth.verification.request.VerificationRequest
import com.ragnarok.auth.verification.service.VerificationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class VerificationController(private val verificationService: VerificationService) {

    @PostMapping("/v1/verification/join")
    fun generateVerificationForJoin(@RequestBody @Valid request: VerificationRequest): ResponseEntity<SingleResponse<EmptyContent>> {
        verificationService.generateJoinVerification(request.toGenerateRequest())

        return ResponseEntity.ok()
            .body(SingleResponse.Ok(EmptyContent()))
    }

    @PostMapping("/v1/verification/reset")
    fun generateVerificationForPasswordReset(@RequestBody @Valid request: VerificationRequest): ResponseEntity<SingleResponse<EmptyContent>> {
        verificationService.generateResetVerification(request.toGenerateRequest())

        return ResponseEntity(SingleResponse.Ok(EmptyContent()), HttpStatus.CREATED)
    }

    @PatchMapping("/v1/verification")
    fun confirmingVerification(@RequestBody @Valid request: VerificationConfirmRequest): ResponseEntity<SingleResponse<EmptyContent>> {
        verificationService.confirmVerification(request.toConfirmRequest())

        return ResponseEntity.ok()
            .body(SingleResponse.Ok(EmptyContent()))
    }
}
