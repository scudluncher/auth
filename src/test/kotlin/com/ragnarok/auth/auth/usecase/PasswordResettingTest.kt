package com.ragnarok.auth.auth.usecase

import com.ragnarok.auth.common.support.FakeHashingProvider
import com.ragnarok.auth.member.domain.repository.FakeMemberRepository
import com.ragnarok.auth.verification.domain.repository.FakeVerificationRepository
import com.ragnarok.auth.verification.exception.NotExistingVerification
import com.ragnarok.auth.verification.exception.OngoingVerificationException
import com.ragnarok.auth.verification.exception.ValidVerificationTimeOverException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class PasswordResettingTest : BehaviorSpec({
    given("비밀번호 초기화 인증이 된 상태에서") {
        val phoneNumber = "01022222222"
        val newPassword = "asdfqwer"

        val request = ResettingRequest(
            phoneNumber,
            newPassword
        )

        val passwordResetting = preparePasswordResetting(request)
        When("새로운 비밀번호와 전화번호를 입력하여 비밀번호를 재설정하면") {
            val result = passwordResetting.execute()
            then("비밀번호가 변경된다.") {
                result.password.hashed shouldBe "salt" + "asdfqwer"
            }
        }
    }

    given("인증한 전화번호와 다른 전화 번호를 입력하거나, 다른 인증타입으로") {
        val phoneNumber = "01022223333"
        val newPassword = "asdfqwer"

        val request = ResettingRequest(
            phoneNumber,
            newPassword
        )

        val passwordResetting = preparePasswordResetting(request)
        When("새로운 비밀번호와 전화번호를 입력하여 비밀번호를 재설정하면") {
            then("해당하는 인증이 없음을 알린다. ") {
                shouldThrow<NotExistingVerification> { passwordResetting.execute() }
            }
        }
    }

    given("비밀번호 초기화 인증이 완료되지 않은 상태에서") {
        val phoneNumber = "01011111111"
        val newPassword = "asdfqwer"

        val request = ResettingRequest(
            phoneNumber,
            newPassword
        )

        val passwordResetting = preparePasswordResetting(request)
        When("새로운 비밀번호와 전화번호를 입력하여 비밀번호를 재설정하면") {
            then("완료되지 않은 인증이 있음을 알린다.") {
                shouldThrow<OngoingVerificationException> { passwordResetting.execute() }
            }
        }
    }

    given("인증을 완료하고 인증 기한을 넘겨버린 상태로") {
        val phoneNumber = "01033334444"
        val newPassword = "asdfqwer"

        val request = ResettingRequest(
            phoneNumber,
            newPassword
        )

        val passwordResetting = preparePasswordResetting(request)
        When("새로운 비밀번호와 전화번호를 입력하여 비밀번호를 재설정하면") {
            then("인증이 완료되었음을 알린다.") {
                shouldThrow< ValidVerificationTimeOverException> { passwordResetting.execute()}
            }
        }
    }

}) {
    companion object {
        val memberRepository = FakeMemberRepository()
        val verificationRepository = FakeVerificationRepository()
        val hashingProvider = FakeHashingProvider()

        fun preparePasswordResetting(request: ResettingRequest): PasswordResetting {
            return PasswordResetting(
                request,
                memberRepository,
                verificationRepository,
                hashingProvider
            )
        }
    }
}
