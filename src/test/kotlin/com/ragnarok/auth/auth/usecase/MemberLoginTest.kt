package com.ragnarok.auth.auth.usecase

import com.ragnarok.auth.auth.exception.AuthenticationFailException
import com.ragnarok.auth.common.exception.BadRequestException
import com.ragnarok.auth.common.support.FakeHashingProvider
import com.ragnarok.auth.member.domain.repository.FakeMemberRepository
import com.ragnarok.auth.member.domain.value.Email
import com.ragnarok.auth.member.exception.NoMemberFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class MemberLoginTest : BehaviorSpec({
    given("식별가능한 정보 (generated ID, 이메일, 전화번호, 별명)와 올바른 비밀번호를 이용하여") {
        val phoneNumber = "01011111111"
        val password = "qwer"

        val request = MemberLoginRequest(
            phoneNumber = phoneNumber,
            password = password,
        )

        val memberLogin = prepareMemberLogin(request)
        When("로그인을 시도하면") {
            val result = memberLogin.execute()
            then("로그인 성공 처리한다.") {
                result.phoneNumber shouldBe phoneNumber
            }
        }
    }

    given("식별가능한 정보 복수개를(generated ID, 이메일, 전화번호, 별명)와 올바른 비밀번호를 이용하여") {
        val phoneNumber = "01011111111"
        val email = Email("cretos@olympus.com")
        val password = "qwer"

        val request = MemberLoginRequest(
            phoneNumber = phoneNumber,
            password = password,
        )

        val memberLogin = prepareMemberLogin(request)
        When("로그인을 시도하면") {
            val result = memberLogin.execute()
            then("로그인 성공 처리한다.") {
                result.phoneNumber shouldBe phoneNumber
                result.email.address shouldBe email.address
            }
        }
    }

    given("식별가능한 정보 (generated ID, 이메일, 전화번호, 별명)와 잘못된 비밀번호를 이용하여") {
        val phoneNumber = "01011111111"
        val password = "qwer33333"

        val request = MemberLoginRequest(
            phoneNumber = phoneNumber,
            password = password,
        )

        val memberLogin = prepareMemberLogin(request)
        When("로그인을 시도하면") {
            then("로그인이 실패하였음을 알린다.") {
                shouldThrow<AuthenticationFailException> { memberLogin.execute() }
            }
        }
    }

    given("없는 회원의 식별정보를 이용하여") {
        val phoneNumber = "01011111112"
        val password = "qwer33333"

        val request = MemberLoginRequest(
            phoneNumber = phoneNumber,
            password = password,
        )

        val memberLogin = prepareMemberLogin(request)
        When("로그인을 시도하면") {
            then("회원이 없음을 알린다.") {
                shouldThrow<NoMemberFoundException> { memberLogin.execute() }
            }
        }
    }

    given("두개의 회원과 일치하는 식별가능한 정보 (generated ID, 이메일, 전화번호, 별명)와 올바른 비밀번호를 이용하여") {
        val phoneNumber = "01011111111"
        val email = Email("atreus@northern.com")
        val password = "qwer33333"

        val request = MemberLoginRequest(
            phoneNumber = phoneNumber,
            email = email,
            password = password,
        )

        val memberLogin = prepareMemberLogin(request)
        When("로그인을 시도하면") {
            then("잘못된 요청임을 알린다.") {
                shouldThrow<BadRequestException> { memberLogin.execute() }
            }
        }
    }
}) {
    companion object {
        val memberRepository = FakeMemberRepository()
        val hashingProvider = FakeHashingProvider()

        fun prepareMemberLogin(request: MemberLoginRequest): MemberLogin {
            return MemberLogin(
                request,
                memberRepository,
                hashingProvider
            )
        }
    }
}
