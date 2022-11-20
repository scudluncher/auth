package com.ragnarok.auth.member.controller

import com.ragnarok.auth.auth.request.PasswordResetRequest
import com.ragnarok.auth.common.infra.controller.ControllerTestExtension
import com.ragnarok.auth.common.response.ErrorResponse
import com.ragnarok.auth.common.response.SingleResponse
import com.ragnarok.auth.member.domain.entity.Member
import com.ragnarok.auth.member.domain.value.Email
import com.ragnarok.auth.member.domain.value.HashedPassword
import com.ragnarok.auth.member.exception.AlreadyUsedEmailException
import com.ragnarok.auth.member.exception.AlreadyUsedNickNameException
import com.ragnarok.auth.member.exception.AlreadyUsedPhoneNumberException
import com.ragnarok.auth.member.request.JoinRequest
import com.ragnarok.auth.member.service.MemberService
import com.ragnarok.auth.member.viewmodel.MyInformation
import com.ragnarok.auth.verification.exception.NotExistingVerificationException
import com.ragnarok.auth.verification.exception.OngoingVerificationException
import com.ragnarok.auth.verification.exception.ValidVerificationTimeOverException
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.AnnotationSpec
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.stub
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

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

        val request = JoinRequest(
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

        val expectedResponse = objectMapper.writeValueAsString(
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
            .andExpect(content().json(expectedResponse))
    }

    @Test
    fun joinFailedDueToWrongArgument() {
        val email = "scudluncher@gmail.com"
        val password = "asdfqwer"
        val name = "myName"
        val nickName = "myNickName"
        val wrongPhoneNumberType = "010-8976-4629"

        val request = JoinRequest(
            email,
            password,
            name,
            nickName,
            wrongPhoneNumberType
        )
        val content = objectMapper.writeValueAsString(request.toJoinRequest())

        mockMvc.perform(
            post("/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun joinFailedDueToNotUniquePhoneNumber() {
        val content = objectMapper.writeValueAsString(joinRequest())

        val expectedErrorResult = objectMapper.writeValueAsString(
            ErrorResponse(
                "not_unique",
                "이미 사용중인 전화번호 입니다."
            )
        )

        memberService.stub {
            on { join(any()) } doThrow AlreadyUsedPhoneNumberException()
        }

        mockMvc.perform(
            post("/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResult))
    }

    @Test
    fun joinFailedDueToNotUniqueEmail() {
        val content = objectMapper.writeValueAsString(joinRequest())

        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "not_unique",
                "이미 사용중인 이메일 입니다."
            )
        )

        memberService.stub {
            on { join(any()) } doThrow AlreadyUsedEmailException()
        }

        mockMvc.perform(
            post("/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun joinFailedDueToNotUniqueNickName() {
        val content = objectMapper.writeValueAsString(joinRequest())

        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "not_unique",
                "이미 사용중인 별명 입니다."
            )
        )

        memberService.stub {
            on { join(any()) } doThrow AlreadyUsedNickNameException()
        }

        mockMvc.perform(
            post("/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun noVerificationForJoinFound() {
        val content = objectMapper.writeValueAsString(joinRequest())

        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "verification_not_found",
                "찾을 수 없는 인증입니다."
            )
        )

        memberService.stub {
            on { join(any()) } doThrow NotExistingVerificationException()
        }

        mockMvc.perform(
            post("/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isNotFound)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun ongoingVerificationForJoinFound() {
        val content = objectMapper.writeValueAsString(joinRequest())

        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "verification_ongoing",
                "진행중인 인증이 있습니다."
            )
        )

        memberService.stub {
            on { join(any()) } doThrow OngoingVerificationException()
        }

        mockMvc.perform(
            post("/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun verificationForJoinIsExpired() {
        val content = objectMapper.writeValueAsString(joinRequest())

        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "verification_expired",
                "유효 인증 시간을 초과하였습니다."
            )
        )

        memberService.stub {
            on { join(any()) } doThrow ValidVerificationTimeOverException()
        }

        mockMvc.perform(
            post("/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }

    private fun joinRequest(): JoinRequest {
        val email = "scudluncher@gmail.com"
        val password = "asdfqwer"
        val name = "myName"
        val nickName = "myNickName"
        val phoneNumber = "01089764629"

        return JoinRequest(
            email,
            password,
            name,
            nickName,
            phoneNumber
        )
    }

    @Test
    fun passwordResettingSuccess() {
        val content = objectMapper.writeValueAsString(resetRequest())

        val passwordChangeMember = Member(
            130L,
            Email("scudluncher@gmail.com"),
            HashedPassword("asdf", "qwer"),
            "YJ SHIN",
            "K810 HUNTER",
            "01089764629"
        )

        val expectedResponse = objectMapper.writeValueAsString(
            SingleResponse.Ok(MyInformation(passwordChangeMember))
        )

        memberService.stub {
            on { resetPassword(any()) } doReturn passwordChangeMember
        }

        mockMvc.perform(
            patch("/v1/members/password-reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isAccepted)
            .andExpect(content().json(expectedResponse))
    }

    @Test
    fun noVerificationForPasswordResettingFound() {
        val content = objectMapper.writeValueAsString(resetRequest())

        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "verification_not_found",
                "찾을 수 없는 인증입니다."
            )
        )

        memberService.stub {
            on { resetPassword(any()) } doThrow NotExistingVerificationException()
        }

        mockMvc.perform(
            patch("/v1/members/password-reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isNotFound)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun ongoingVerificationForPasswordResettingFound() {
        val content = objectMapper.writeValueAsString(resetRequest())

        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "verification_ongoing",
                "진행중인 인증이 있습니다."
            )
        )

        memberService.stub {
            on { resetPassword(any()) } doThrow OngoingVerificationException()
        }

        mockMvc.perform(
            patch("/v1/members/password-reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun verificationForPasswordResettingIsExpired() {
        val content = objectMapper.writeValueAsString(resetRequest())

        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "verification_expired",
                "유효 인증 시간을 초과하였습니다."
            )
        )

        memberService.stub {
            on { resetPassword(any()) } doThrow ValidVerificationTimeOverException()
        }

        mockMvc.perform(
            patch("/v1/members/password-reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }

    private fun resetRequest(): PasswordResetRequest {
        val phoneNumber = "01089764629"
        val newPassword = "194nfvne"

        return PasswordResetRequest(
            phoneNumber,
            newPassword
        )
    }
}
