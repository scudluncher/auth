package com.ragnarok.auth.verification.usecase

import com.ragnarok.auth.verification.domain.repository.FakeVerificationRepository
import com.ragnarok.auth.verification.domain.value.VerificationType
import com.ragnarok.auth.verification.exception.CodeExpiredException
import com.ragnarok.auth.verification.exception.CodeNotMatchedException
import com.ragnarok.auth.verification.exception.NotExistingVerificationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ConfirmingVerificationTest : BehaviorSpec({

    given("올바른 코드가 주어지고") {
        val phoneNumber = "01055555555"
        val code = "555555"
        val type = VerificationType.JOIN
        val request = VerificationConfirmRequest(
            phoneNumber,
            code,
            type
        )

        val confirmingVerification = prepareConfirmingVerification(request)
        When("사용자가 입력하면") {
            confirmingVerification.execute()
            then("유효 인증이 생성된다.") {
                val verification = verificationRepository.findByPhoneNumberAndType(phoneNumber, type)
                verification?.verified shouldBe true
            }
        }
    }

    given("잘못된 전화번호나 인증타입을 선택하고") {
        val phoneNumber = "01055554444"
        val code = "555555"
        val type = VerificationType.JOIN
        val request = VerificationConfirmRequest(
            phoneNumber,
            code,
            type
        )

        val confirmingVerification = prepareConfirmingVerification(request)
        When("사용자가 입력하면") {
            then("해당하는 인증이 없음을 알린다") {
                shouldThrow<NotExistingVerificationException> { confirmingVerification.execute() }
            }
        }
    }

    given("잘못된 인증코드를") {
        val phoneNumber = "01055555555"
        val code = "123456"
        val type = VerificationType.JOIN
        val request = VerificationConfirmRequest(
            phoneNumber,
            code,
            type
        )

        val confirmingVerification = prepareConfirmingVerification(request)
        When("사용자가 입력하면") {
            then("코드가 잘못되었음을 알린다.") {
                shouldThrow<CodeNotMatchedException> { confirmingVerification.execute() }
            }
        }
    }

    given("인증코드를") {
        val phoneNumber = "01099999999"
        val code = "111111"
        val type = VerificationType.JOIN
        val request = VerificationConfirmRequest(
            phoneNumber,
            code,
            type
        )

        val confirmingVerification = prepareConfirmingVerification(request)
        When("너무 늦게 입력하면") {
            then("코드가 되었음을 알린다.") {
                shouldThrow<CodeExpiredException> { confirmingVerification.execute() }
            }
        }
    }
}) {
    companion object {
        val verificationRepository = FakeVerificationRepository()

        fun prepareConfirmingVerification(request: VerificationConfirmRequest): ConfirmingVerification {
            return ConfirmingVerification(
                request,
                verificationRepository
            )
        }
    }
}
