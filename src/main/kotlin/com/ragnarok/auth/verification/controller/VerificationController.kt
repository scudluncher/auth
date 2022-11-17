package com.ragnarok.auth.verification.controller

import com.ragnarok.auth.common.response.EmptyContent
import com.ragnarok.auth.verification.request.VerificationRequest
import com.ragnarok.auth.verification.service.VerificationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class VerificationController(private val verificationService: VerificationService) {

    @PostMapping("/join-verification")
    fun generateVerification(@Valid request: VerificationRequest): ResponseEntity<EmptyContent> {
        verificationService.generateVerification(request.toGenerateRequest())

        return ResponseEntity<EmptyContent>(HttpStatus.CREATED)
    }

    @PatchMapping("/verification")
    fun confirmingVerification() {
        TODO("yessss")
    }
}
