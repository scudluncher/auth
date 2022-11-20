package com.ragnarok.auth.member.controller

import com.ragnarok.auth.common.infra.controller.ControllerTestExtension
import com.ragnarok.auth.common.response.ErrorResponse
import com.ragnarok.auth.common.response.SingleResponse
import com.ragnarok.auth.member.domain.entity.Member
import com.ragnarok.auth.member.domain.value.Email
import com.ragnarok.auth.member.domain.value.HashedPassword
import com.ragnarok.auth.member.service.MemberService
import com.ragnarok.auth.member.viewmodel.MyInformation
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.AnnotationSpec
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MemberController::class)
class MemberControllerTest : ControllerTestExtension, AnnotationSpec() {
    @InjectMocks
    private lateinit var memberController: MemberController

    @Mock
    private lateinit var memberService: MemberService

    private lateinit var mockMvc: MockMvc

    private val objectMapper = objectMapper()

    override suspend fun beforeSpec(spec: Spec) {
        MockitoAnnotations.openMocks(this)
        mockMvc = defaultMockMvcBuilder(memberController)
            .build()
    }

    @Test
    fun getMyInfoSuccess() {
        val me = Member(
            130L,
            Email("scudluncher@gmail.com"),
            HashedPassword("asdf", "qwer"),
            "YJ SHIN",
            "K810 HUNTER",
            "01089764629"
        )

        val expectedResponse = objectMapper.writeValueAsString(
            SingleResponse.Ok(MyInformation(me))
        )

        memberService.stub {
            on { me(any()) } doReturn me
        }

        setSecurityContext()

        mockMvc.perform(
            get("/v1/members/me")
                .header("Authorization", "Bearer SOME_ACCESS_TOKEN")
        )
            .andExpect(status().isOk)
            .andExpect(content().json(expectedResponse))
    }

    @Test
    fun getMyInfoFailedDueToAuthorization() {
        val me = Member(
            130L,
            Email("scudluncher@gmail.com"),
            HashedPassword("asdf", "qwer"),
            "YJ SHIN",
            "K810 HUNTER",
            "01089764629"
        )

        val expectedResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "authorization_fail",
                "인가 중 문제가 발생했습니다."
            )
        )

        memberService.stub {
            on { me(any()) } doReturn me
        }

        mockMvc.perform(
            get("/v1/members/me")
        )
            .andExpect(status().isUnauthorized)
            .andExpect(content().json(expectedResponse))
    }

    @Test
    fun joinSuccess() {
        val email = "scudluncher@gmail.com"
        val password = "asdfqwer"
        val name = "myName"
        val nickName = "myNickName"
        val phoneNumber = "01089764629"

        val request = com.ragnarok.auth.member.request.JoinRequest(
            email,
            password,
            name,
            nickName,
            phoneNumber
        )

        val content = objectMapper.writeValueAsString(request)

        val expectedMember = Member(
            33L,
            Email(email),
            HashedPassword("wwww", "zxcv"),
            name,
            nickName,
            phoneNumber
        )

        val expectedResult = objectMapper.writeValueAsString(
            SingleResponse.Ok(MyInformation(expectedMember))
        )

        memberService.stub {
            on { join(any()) } doReturn expectedMember
        }

        mockMvc.perform(
            post("/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(expectedResult))
    }

    @Test
    fun joinFailedDueToWrongArgument() {
        val email = "scudluncher@gmail.com"
        val tooShortPassword = "1"
        val name = "myName"
        val nickName = "myNickName"
        val phoneNumber = "01089764629"

        val request = com.ragnarok.auth.member.request.JoinRequest(
            email,
            tooShortPassword,
            name,
            nickName,
            phoneNumber
        )

        val content = objectMapper.writeValueAsString(request)

        memberService.stub {
            on { join(any()) } doReturn any()
        }

        mockMvc.perform(
            post("/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isBadRequest)
    }


}
