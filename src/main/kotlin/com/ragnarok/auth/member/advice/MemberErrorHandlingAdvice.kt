package com.ragnarok.auth.member.advice

import com.ragnarok.auth.common.response.ErrorResponse
import com.ragnarok.auth.member.exception.AlreadyRegisteredMemberException
import com.ragnarok.auth.member.exception.DuplicatedMemberKeyException
import com.ragnarok.auth.member.exception.NoMemberFoundException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(1)
class MemberErrorHandlingAdvice {
    @ExceptionHandler(AlreadyRegisteredMemberException::class)
    fun alreadyRegisteredMember(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    "duplicated_member",
                    "이미 가입된 회원입니다."
                )
            )
    }

    @ExceptionHandler(DuplicatedMemberKeyException::class)
    fun alreadyRegisteredMember(e: DuplicatedMemberKeyException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    "not_unique",
                    "이미 사용중인 ${e.type} 입니다."
                )
            )
    }

    @ExceptionHandler(NoMemberFoundException::class)
    fun noMemberFound(): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    "not_found_member",
                    "없는 회원입니다."
                )
            )
    }
}
