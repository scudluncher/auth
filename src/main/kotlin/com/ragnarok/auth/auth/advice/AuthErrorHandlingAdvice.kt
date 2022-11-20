package com.ragnarok.auth.auth.advice

import com.ragnarok.auth.auth.exception.AuthenticationFailException
import com.ragnarok.auth.auth.exception.AuthorizationException
import com.ragnarok.auth.auth.exception.NotEnoughIdentificationProvidedException
import com.ragnarok.auth.common.response.ErrorResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(1)
class AuthErrorHandlingAdvice {
    @ExceptionHandler(AuthenticationFailException::class)
    fun authenticationFail(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
                    "login_fail",
                    "식별가능정보와 비밀번호가 일치하지 않습니다."
                )
            )
    }

    @ExceptionHandler(NotEnoughIdentificationProvidedException::class)
    fun notEnoughIdentificationProvided(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    "identification_incomplete",
                    "식별가능정보가 부정확합니다."
                )
            )
    }

    @ExceptionHandler(AuthorizationException::class)
    fun authorizationFail(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
                    "authorization_fail",
                    "인가 중 문제가 발생했습니다."
                )
            )
    }
}
