package com.ragnarok.auth.common.infra

import com.ragnarok.auth.common.support.TokenPayload
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class JwtTokenProviderTest : BehaviorSpec({
    given("payload 값으로") {
        val tokenProvider = JwtTokenProvider(InMemoryJwsKeyStorage())
        val id = 1L
        val nickName = "Son Of Zeus"
        val payload = TokenPayload(
            id,
            nickName
        )
        When("토큰을 생성하고, 토큰을 다시 파싱하면") {
            val token = tokenProvider.token(payload)
            then("동일한 값이 보장된다.") {
            tokenProvider.payload(token) shouldBe payload
            }
        }
    }
})
