package com.ragnarok.auth.verification.infra.repository

import com.ragnarok.auth.verification.domain.value.VerificationType
import com.ragnarok.auth.verification.infra.entity.VerificationRedisEntity
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class RedisVerificationCrudRepositoryTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var redisVerificationCrudRepository: RedisVerificationCrudRepository

    override suspend fun beforeSpec(spec: Spec) {
        val verifications = listOf(
            VerificationRedisEntity(
                null,
                "01089764629",
                "123456",
                LocalDateTime.now().plusMinutes(3),
                VerificationType.JOIN,
                null,
                false
            ),
            VerificationRedisEntity(
                null,
                "01089764629",
                "121256",
                LocalDateTime.now().plusMinutes(10),
                VerificationType.JOIN,
                null,
                false
            ),
            VerificationRedisEntity(
                null,
                "01012345678",
                "234568",
                LocalDateTime.now().plusMinutes(3),
                VerificationType.RESET,
                null,
                false
            ),
        )

        redisVerificationCrudRepository.saveAll(verifications)
    }

    init {
        this.given("전화번호와 인증 타입이 주어지고") {
            val phoneNumber = "01089764629"
            val type = VerificationType.JOIN

            When("검색하면") {
                val result = redisVerificationCrudRepository.findFirstByPhoneNumberAndTypeOrderByCodeExpiredTimeDesc(
                    phoneNumber,
                    type
                )
                then("코드 만료 내림차순으로 하나의 인증이 조회된다.") {
                    result shouldNotBe null
                    result?.let {
                        it.phoneNumber shouldBe phoneNumber
                        it.type shouldBe type
                    }
                }
            }
        }
    }
}
