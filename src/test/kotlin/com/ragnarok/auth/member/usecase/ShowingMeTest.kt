package com.ragnarok.auth.member.usecase

import com.ragnarok.auth.member.domain.repository.FakeMemberRepository
import com.ragnarok.auth.member.exception.NoMemberFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ShowingMeTest : BehaviorSpec({
    given("로그인된 상태의 사용자가") {
        val id = 1L
        val showingMe = ShowingMe(
            id,
            FakeMemberRepository()
        )
        When("내 정보를 조회하면 ") {
            val result = showingMe.retrieve()
            then("내 정보가 조회된다.") {
                result.id shouldBe id
                result.nickName shouldNotBe null
                result.email shouldNotBe null
                result.name shouldNotBe null
                result.phoneNumber shouldNotBe null
            }
        }
    }

    given("비정상적 사용자가") {
        val id = 7777L
        val showingMe = ShowingMe(
            id,
            FakeMemberRepository()
        )
        When("없는 ID 를 조회하면 ") {
            then("회원이 없음을 알린다.") {
                shouldThrow<NoMemberFoundException> { showingMe.retrieve() }
            }
        }
    }
})
