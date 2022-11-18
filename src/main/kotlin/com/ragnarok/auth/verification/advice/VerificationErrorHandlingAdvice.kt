package com.ragnarok.auth.verification.advice

import com.ragnarok.auth.common.response.ErrorResponse
import com.ragnarok.auth.verification.exception.ActiveVerificationExistException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class VerificationErrorHandlingAdvice {
    @ExceptionHandler(ActiveVerificationExistException::class)
    fun activeVerificationExist(): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse("verification_ongoing", "진행중인 인증이 있습니다."),
            HttpStatus.CONFLICT
        )
    }
}
