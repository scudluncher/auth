package com.ragnarok.auth.verification.usecase

import com.ragnarok.auth.common.infra.FakeSmsSender
import com.ragnarok.auth.member.domain.repository.FakeMemberRepository
import com.ragnarok.auth.member.exception.NoMemberFoundException
import com.ragnarok.auth.verification.domain.repository.FakeVerificationRepository
import com.ragnarok.auth.verification.domain.value.VerificationType
import com.ragnarok.auth.verification.exception.ActiveVerificationExistException
import com.ragnarok.auth.verification.exception.AlreadyVerifiedException
import com.ragnarok.auth.verification.request.VerificationRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime

class GeneratingResetVerificationTest : BehaviorSpec({
    given("기존 가입자가 비밀번호 재설정 인증을 위해 전화번호를 제출하고") {
        val phoneNumber = "01033333333"
        val generatingResetVerification = prepareResetVerification(phoneNumber)
        When("인증 코드를 생성하면") {
            generatingResetVerification.execute()
            then("해당 번호로 비밀번호 재설정용 Verification 이 생성된다.") {
                val verification = verificationRepository.findByPhoneNumberAndType(
                    phoneNumber,
                    VerificationType.RESET
                )
                verification shouldNotBe null
                verification?.let {
                    it.phoneNumber shouldBe phoneNumber
                    it.codeExpiredTime shouldBeAfter LocalDateTime.now()
                    it.verified shouldBe false
                    it.verificationExpiredTime shouldBe null
                }
            }
        }
    }

    given("기존 가입자가 비밀번호 재설정 인증을 완료 하였으나, 인증 완료 시간이 지난 후 다시 전화번호를 제출하고") {
        val phoneNumber = "01012345678"
        val generatingResetVerification = prepareResetVerification(phoneNumber)
        When("인증 코드를 생성하면") {
            generatingResetVerification.execute()
            then("해당 번호로 비밀번호 재설정용 Verification 이 생성된다.") {
                val verification = verificationRepository.findByPhoneNumberAndType(
                    phoneNumber,
                    VerificationType.RESET
                )
                verification shouldNotBe null
                verification?.let {
                    it.phoneNumber shouldBe phoneNumber
                    it.codeExpiredTime shouldBeAfter LocalDateTime.now()
                    it.verified shouldBe false
                    it.verificationExpiredTime shouldBe null
                }
            }
        }
    }

    given("미 가입자가 비밀번호 재설정 인증을 위해 전화번호를 제출하고") {
        val phoneNumber = "01098642728"
        val generatingResetVerification = prepareResetVerification(phoneNumber)
        When("인증 코드를 생성하려 하면") {
            then("미가입 회원임을 알린다.") {
                shouldThrow<NoMemberFoundException> { generatingResetVerification.execute() }
            }
        }
    }

    given("비밀번호 재설정 인증을 위해 이미 전화번호를 제출한 가입자가 전화번호를 제출하고") {
        val phoneNumber = "01011111111"
        val generatingResetVerification = prepareResetVerification(phoneNumber)
        When("인증 코드를 생성하려 하면") {
            then("진행중인 인증이 존재함을 알린다.") {
                shouldThrow<ActiveVerificationExistException> { generatingResetVerification.execute() }
            }
        }
    }

    given("비밀번호 재설정 인증이 되어 비밀번호 재설정이 가능한 가입자가 전화번호를 제출하고") {
        val phoneNumber = "01022222222"
        val generatingResetVerification = prepareResetVerification(phoneNumber)
        When("인증 코드를 생성하려 하면") {
            then("현재 인증 상태로 비밀번호 재설정이 가능한 상태임을 알린다.") {
                shouldThrow<AlreadyVerifiedException> { generatingResetVerification.execute() }
            }
        }
    }
}) {
    companion object {
        val verificationRepository = FakeVerificationRepository()
        val memberRepository = FakeMemberRepository()
        val messageSender = FakeSmsSender()

        fun prepareResetVerification(phoneNumber: String): GeneratingResetVerification {
            return GeneratingResetVerification(
                VerificationRequest(phoneNumber).toGenerateRequest(),
                verificationRepository,
                memberRepository,
                messageSender
            )
        }
    }
}
