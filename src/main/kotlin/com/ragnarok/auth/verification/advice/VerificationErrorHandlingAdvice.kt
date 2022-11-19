package com.ragnarok.auth.verification.advice

import com.ragnarok.auth.common.response.ErrorResponse
import com.ragnarok.auth.verification.exception.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class VerificationErrorHandlingAdvice {
    @ExceptionHandler(OngoingVerificationException::class)
    fun ongoingVerificationExist(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse(
                "verification_ongoing",
                "진행중인 인증이 있습니다."
            ))
    }

    @ExceptionHandler(AlreadyVerifiedException::class)
    fun alreadyVerified(e: AlreadyVerifiedException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse(
                "verification_valid",
                "${e.verificationExpiredTime} 까지 유효한 인증이 있습니다."
            ))
    }

    @ExceptionHandler(NotExistingVerification::class)
    fun notExistingVerification(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(
                "verification_not_found",
                "찾을 수 없는 인증입니다."
            ))
    }

    @ExceptionHandler(ValidVerificationTimeOverException::class)
    fun validVerificationTimeOver(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse(
                "verification_expired",
                "유효 인증 시간을 초과하였습니다."
            ))
    }

    @ExceptionHandler(CodeNotMatchedException::class)
    fun codeNotMatched(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse(
                "code_not_matched",
                "코드가 매치하지 않습니다."
            ))
    }

    @ExceptionHandler(CodeExpiredException::class)
    fun codeExpired(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse(
                "code_expired",
                "코드 입력 시간이 지났습니다."
            ))
    }
}
