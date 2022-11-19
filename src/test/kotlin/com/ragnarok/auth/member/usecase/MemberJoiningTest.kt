package com.ragnarok.auth.member.usecase

import com.ragnarok.auth.common.support.FakeHashingProvider
import com.ragnarok.auth.common.support.HashingProvider
import com.ragnarok.auth.member.domain.repository.FakeMemberRepository
import com.ragnarok.auth.member.domain.repository.MemberRepository
import com.ragnarok.auth.member.exception.AlreadyUsedEmailException
import com.ragnarok.auth.member.exception.AlreadyUsedNickNameException
import com.ragnarok.auth.member.exception.AlreadyUsedPhoneNumberException
import com.ragnarok.auth.verification.domain.repository.FakeVerificationRepository
import com.ragnarok.auth.verification.domain.repository.VerificationRepository
import com.ragnarok.auth.verification.exception.NotExistingVerification
import com.ragnarok.auth.verification.exception.OngoingVerificationException
import com.ragnarok.auth.verification.exception.ValidVerificationTimeOverException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class MemberJoiningTest : BehaviorSpec({
    given("인증된 상태의 가입자가 가입정보를 제출하고") {
        val email = "frodo@beggins.com"
        val phoneNumber = "01066666666"
        val password = "378jfje"
        val name = "frodo"
        val nickName = "ring destroyer"

        val request = JoinRequest(
            email,
            password,
            name,
            nickName,
            phoneNumber
        )

        val memberJoining = prepareMemberJoin(request)
        When("회원 가입하면") {
            val result = memberJoining.execute()
            then("회원으로 가입된다.") {
                result.phoneNumber shouldBe phoneNumber
                result.email.address shouldBe email
                result.shouldNotBe(password)  // because it is encrypted!
                result.nickName shouldBe nickName
            }
        }
    }

    given("인증을 아예 안한 가입자가 가입정보를 제출하고") {
        val email = "heimdall@asgard.com"
        val phoneNumber = "01099775533"
        val password = "378jf33333"
        val name = "heimdall"
        val nickName = "farseer"

        val request = JoinRequest(
            email,
            password,
            name,
            nickName,
            phoneNumber
        )

        val memberJoining = prepareMemberJoin(request)
        When("회원 가입하면") {
            then("미인증 상태임을 알린다.") {
                shouldThrow<NotExistingVerification> { memberJoining.execute() }
            }
        }
    }

    given("인증 코드를 제출하지 않은 상태에서 가입자가 가입정보를 제출하고") {
        val email = "heimdall@asgard.com"
        val phoneNumber = "01055555555"
        val password = "378jf33333"
        val name = "heimdall"
        val nickName = "farseer"

        val request = JoinRequest(
            email,
            password,
            name,
            nickName,
            phoneNumber
        )

        val memberJoining = prepareMemberJoin(request)
        When("회원 가입하면") {
            then("코드 선제출이 필요함을 알린다.") {
                shouldThrow<OngoingVerificationException> { memberJoining.execute() }
            }
        }
    }

    given("인증 코드를 제출하였으나 유효 인증 시간이 지나고 가입자가 가입정보를 제출하고") {
        val email = "jormungandr@jotunheim.com"
        val phoneNumber = "01099999999"
        val password = "378jf33222333"
        val name = "jormungandr"
        val nickName = "world serpent"

        val request = JoinRequest(
            email,
            password,
            name,
            nickName,
            phoneNumber
        )

        val memberJoining = prepareMemberJoin(request)
        When("회원 가입하면") {
            then("유효 인증 기한이 지났음을 알린다.") {
                shouldThrow<ValidVerificationTimeOverException> { memberJoining.execute() }
            }
        }
    }

    given("중복된 전화번호로 가입정보를 제출하고") {
        val email = "cretos@again.com"
        val phoneNumber = "01011111111"
        val password = "378jf33222333"
        val name = "cretos duplicated"
        val nickName = "poor cretos"

        val request = JoinRequest(
            email,
            password,
            name,
            nickName,
            phoneNumber
        )
        val memberJoining = prepareMemberJoin(request)
        When("회원 가입하면") {
            then("중복된 전화번호임을 알린다.") {
                shouldThrow<AlreadyUsedPhoneNumberException> { memberJoining.execute() }
            }
        }
    }

    given("중복된 이메일로 가입정보를 제출하고") {
        val email = "cretos@olympus.com"
        val phoneNumber = "01078783434"
        val password = "378jf33222333"
        val name = "cretos duplicated"
        val nickName = "poor cretos"

        val request = JoinRequest(
            email,
            password,
            name,
            nickName,
            phoneNumber
        )
        val memberJoining = prepareMemberJoin(request)
        When("회원 가입하면") {
            then("중복된 이메일임을 알린다.") {
                shouldThrow<AlreadyUsedEmailException> { memberJoining.execute() }
            }
        }
    }

    given("중복된 별명으로 가입정보를 제출하고") {
        val email = "cretos@egypt.com"
        val phoneNumber = "01078783434"
        val password = "378jf33222333"
        val name = "cretos duplicated"
        val nickName = "Son Of Zeus"

        val request = JoinRequest(
            email,
            password,
            name,
            nickName,
            phoneNumber
        )
        val memberJoining = prepareMemberJoin(request)
        When("회원 가입하면") {
            then("중복된 별명임을 알린다.") {
                shouldThrow<AlreadyUsedNickNameException> { memberJoining.execute() }
            }
        }
    }
}) {
    companion object {
        val memberRepository: MemberRepository = FakeMemberRepository()
        val verificationRepository: VerificationRepository = FakeVerificationRepository()
        val hashingProvider: HashingProvider = FakeHashingProvider()

        fun prepareMemberJoin(request: JoinRequest): MemberJoining {
            return MemberJoining(
                request,
                memberRepository,
                verificationRepository,
                hashingProvider
            )
        }
    }
}
