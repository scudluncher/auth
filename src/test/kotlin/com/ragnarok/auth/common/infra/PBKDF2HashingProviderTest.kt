package com.ragnarok.auth.common.infra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class PBKDF2HashingProviderTest : BehaviorSpec({
    given("hashing provider가") {
        val firstSalt = hashingProvider.salt()
        When("매번 제공하는 salt가") {
            val secondSalt = hashingProvider.salt()
            then("다름을 확인한다") {
                firstSalt shouldNotBe secondSalt
            }
        }
    }

    given("패스워드와 salt가 주어지고") {
        val password = "asdfqwer"
        val salt = hashingProvider.salt()
        When("동일한 password & salt 조합으로 hashing 을 진행하면") {
            val firstHashed = hashingProvider.hash(password, salt)
            val secondHashed = hashingProvider.hash(password, salt)
            then("같은 hashed 값이 나온다.") {
                firstHashed shouldBe secondHashed
            }
        }
    }

    given("두개의 패스워드와 1개의 salt가 주어지고") {
        val firstPassword = "asdfqwer"
        val secondPassword = "zxcvlkje"
        val salt = hashingProvider.salt()
        When("동일한 password & salt 조합으로 hashing 을 진행하면") {
            val firstHashed = hashingProvider.hash(firstPassword, salt)
            val secondHashed = hashingProvider.hash(secondPassword, salt)
            then("다른 hashed 값이 나온다.") {
                firstHashed shouldNotBe secondHashed
            }
        }
    }

}) {
    companion object {
        val hashingProvider = PBKDF2HashingProvider()
    }
}
