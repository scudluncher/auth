package com.ragnarok.auth.member.infra.repository

import com.ragnarok.auth.member.infra.entity.MemberJpaEntity
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JpaMemberCrudRepositoryTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var jpaMemberRepository: JpaMemberCrudRepository

    override suspend fun beforeSpec(spec: Spec) {
        val jpaMembers = listOf(
            MemberJpaEntity(
                0L,
                "scudluncher@gmail.com",
                "asdfqwerhas",
                "salt1",
                "YJ SHIN",
                "cat house owner",
                "01089764629"
            ),
            MemberJpaEntity(
                0L,
                "yjshin@deali.net",
                "eewewerwer",
                "salt2",
                "YJ SHIN",
                "safari owner",
                "01033332222"
            )
        )

        jpaMemberRepository.saveAll(jpaMembers)
    }

    init {
        this.given("전화번호를 이용해서") {
            val phoneNumber = "01089764629"
            When("회원을 검색하면") {
                val result = jpaMemberRepository.findByPhoneNumber(phoneNumber)
                then("동일한 전화번호의 회원이 검색된다.") {
                    result shouldNotBe null
                    result?.phoneNumber shouldBe phoneNumber
                }
            }
        }

        this.given("이메일을 이용해서") {
            val email = "yjshin@deali.net"
            When("회원을 검색하면") {
                val result = jpaMemberRepository.findByEmailAddress(email)
                then("동일한 이메일의 회원이 검색된다.") {
                    result shouldNotBe null
                    result?.emailAddress shouldBe email
                }
            }
        }

        this.given("별명을 이용해서") {
            val nickName = "cat house owner"
            When("회원을 검색하면") {
                val result = jpaMemberRepository.findByNickName(nickName)
                then("동일한 이메일의 회원이 검색된다.") {
                    result shouldNotBe null
                    result?.nickName shouldBe nickName
                }
            }
        }
    }
}
