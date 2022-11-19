package com.ragnarok.auth.verification.usecase

import com.ragnarok.auth.common.infra.FakeSmsSender
import com.ragnarok.auth.member.domain.repository.FakeMemberRepository
import com.ragnarok.auth.member.exception.AlreadyRegisteredMemberException
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

class GeneratingJoinVerificationTest : BehaviorSpec({
    given("기존 미가입자가 인증을 위해 전화번호를 제출하고") {
        val phoneNumber = "01088888888"
        val generatingJoinVerification = prepareJoinVerification(phoneNumber)
        When("인증 코드를 생성하면") {
            generatingJoinVerification.execute()
            then("해당 번호로 가입용 Verification 이 생성된다.") {
                val verification = verificationRepository.findByPhoneNumberAndType(
                    phoneNumber,
                    VerificationType.JOIN
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

    given("기존 미가입자가 인증을 완료하였으나, 인증 완료 시간이 지난 후 전화번호를 제출하고") {
        val phoneNumber = "01099999999"
        val generatingJoinVerification = prepareJoinVerification(phoneNumber)
        When("인증 코드를 생성하면") {
            generatingJoinVerification.execute()
            then("해당 번호로 가입용 Verification 이 생성된다.") {
                val verification = verificationRepository.findByPhoneNumberAndType(
                    phoneNumber,
                    VerificationType.JOIN
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

    given("기존 가입자가 가입을 위해 전화번호가 제출하고") {
        val phoneNumber = "01011111111"
        val alreadyMember = memberRepository.findByPhoneNumber(phoneNumber)
        assert(alreadyMember != null)
        val generatingJoinVerification = prepareJoinVerification(phoneNumber)
        When("인증 코드를 생성하려하면") {
            then("이미 가입된 회원임을 알린다.") {
                shouldThrow<AlreadyRegisteredMemberException> { generatingJoinVerification.execute() }
            }
        }
    }

    given("전화번호를 제출하고 인증을 진행하지 않은 미가입자가") {
        val phoneNumber = "01055555555"
        val generatingJoinVerification = prepareJoinVerification(phoneNumber)
        When("인증코드를 생성하려하면") {
            then("진행중인 인증이 존재함을 알린다.") {
                shouldThrow<ActiveVerificationExistException> { generatingJoinVerification.execute() }
            }
        }
    }

    given("인증이 되어 가입 가능한 상태의 미가입자가 전화번호를 제출하고") {
        val phoneNumber = "01066666666"
        val generatingJoinVerification = prepareJoinVerification(phoneNumber)
        When("인증코드를 생성하려하면") {
            then("현재 인증 상태로 가입 가능한 상태임을 알린다.") {
                shouldThrow<AlreadyVerifiedException> { generatingJoinVerification.execute() }
            }
        }
    }
}) {
    companion object {
        val verificationRepository = FakeVerificationRepository()
        val memberRepository = FakeMemberRepository()
        val messageSender = FakeSmsSender()

        fun prepareJoinVerification(phoneNumber: String): GeneratingJoinVerification {
            return GeneratingJoinVerification(
                VerificationRequest(phoneNumber).toGenerateRequest(),
                verificationRepository,
                memberRepository,
                messageSender
            )
        }
    }
}
