package com.ragnarok.auth.common.advice

import com.ragnarok.auth.common.exception.BadRequestException
import com.ragnarok.auth.common.response.ErrorResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class CommonErrorHandlingAdvice {
    @ExceptionHandler(RuntimeException::class)
    fun internalServerError(e: RuntimeException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    "internal_server_error",
                    e.message ?: HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
                )
            )
    }

    @ExceptionHandler(BadRequestException::class)
    fun badRequest(e: BadRequestException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    "bad_request",
                    e.message ?: "잘못된 요청입니다."
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun requestArgumentWrong(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    "wrong_argument",
                    e.bindingResult.allErrors[0].defaultMessage ?: HttpStatus.BAD_REQUEST.reasonPhrase
                )
            )
    }
}
