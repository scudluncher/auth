package com.ragnarok.auth.verification.usecase

import com.ragnarok.auth.common.infra.FakeSmsSender
import com.ragnarok.auth.member.domain.repository.FakeMemberRepository
import com.ragnarok.auth.member.exception.AlreadyRegisteredMemberException
import com.ragnarok.auth.verification.domain.repository.FakeVerificationRepository
import com.ragnarok.auth.verification.domain.value.VerificationType
import com.ragnarok.auth.verification.request.VerificationRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime

class GeneratingResetVerificationTest : BehaviorSpec({
    given("기존 미가입자가 인증을 위해 전화번호를 제출하고") {
        val phoneNumber = "01088888888"
        val request = VerificationRequest(phoneNumber)
        val generatingJoinVerification = GeneratingJoinVerification(
            request.toGenerateRequest(),
            verificationRepository,
            memberRepository,
            messageSender,
        )
        When("인증 코드를 생성하면") {
            generatingJoinVerification.execute()
            then("해당 번호로 가입용 Verification 이 생성된다.") {
                val verification = verificationRepository.findByPhoneNumberAndType(phoneNumber, VerificationType.JOIN)
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

    given("기존 가입자가 가입을 위해 전화번호가 제출하면") {
        val phoneNumber = "01011111111"
        val alreadyMember = memberRepository.findByPhoneNumber(phoneNumber)
        val request = VerificationRequest(phoneNumber)
        assert(alreadyMember != null)
        val generatingJoinVerification = GeneratingJoinVerification(
            request.toGenerateRequest(),
            verificationRepository,
            memberRepository,
            messageSender,
        )
        When("인증 코드를 생성하면") {
            shouldThrow<AlreadyRegisteredMemberException> { generatingJoinVerification.execute() }
            then("이미 가입된 회원 예외가 발생한다.")
        }
    }

    TODO("ActiveVerificationExistException")

    TODO("AlreadyVerifiedException")

}) {
    companion object {
        val verificationRepository = FakeVerificationRepository()
        val memberRepository = FakeMemberRepository()
        val messageSender = FakeSmsSender()
    }
}
