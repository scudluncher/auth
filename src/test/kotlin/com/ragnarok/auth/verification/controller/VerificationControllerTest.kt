package com.ragnarok.auth.verification.controller

import com.ragnarok.auth.common.infra.controller.ControllerTestExtension
import com.ragnarok.auth.common.response.ErrorResponse
import com.ragnarok.auth.member.exception.AlreadyRegisteredMemberException
import com.ragnarok.auth.member.exception.NoMemberFoundException
import com.ragnarok.auth.verification.exception.*
import com.ragnarok.auth.verification.request.VerificationConfirmRequest
import com.ragnarok.auth.verification.request.VerificationRequest
import com.ragnarok.auth.verification.service.VerificationService
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.AnnotationSpec
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.stub
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(VerificationController::class)
class VerificationControllerTest : ControllerTestExtension, AnnotationSpec() {
    @InjectMocks
    private lateinit var verificationController: VerificationController

    @Mock
    private lateinit var verificationService: VerificationService

    private lateinit var mockMvc: MockMvc

    private val objectMapper = objectMapper()

    override suspend fun beforeSpec(spec: Spec) {
        MockitoAnnotations.openMocks(this)
        mockMvc = defaultMockMvcBuilder(verificationController)
            .build()
    }

    @Test
    fun generatingVerificationForJoinSuccess() {
        val phoneNumber = "01089761234"
        val request = VerificationRequest(phoneNumber)

        val content = objectMapper.writeValueAsString(request)


        doNothing().`when`(verificationService).generateJoinVerification(any())

        mockMvc.perform(
            post("/v1/verification/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isCreated)

    }

    @Test
    fun duplicatedMemberJoinVerification() {
        val phoneNumber = "01089761234"
        val request = VerificationRequest(phoneNumber)

        val content = objectMapper.writeValueAsString(request)
        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "duplicated_member",
                "이미 가입된 회원입니다."
            )
        )

        verificationService.stub {
            on { generateJoinVerification(any()) } doThrow AlreadyRegisteredMemberException()
        }

        val result = mockMvc.perform(
            post("/v1/verification/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )

        result.andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun ongoingJoinVerification() {
        val phoneNumber = "01089761234"
        val request = VerificationRequest(phoneNumber)

        val content = objectMapper.writeValueAsString(request)
        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "verification_ongoing",
                "진행중인 인증이 있습니다."
            )
        )

        verificationService.stub {
            on { generateJoinVerification(any()) } doThrow OngoingVerificationException()
        }

        mockMvc.perform(
            post("/v1/verification/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun alreadyVerifiedJoinVerification() {
        val phoneNumber = "01089761234"
        val request = VerificationRequest(phoneNumber)

        val content = objectMapper.writeValueAsString(request)
        val expiredTime = LocalDateTime.now().plusMinutes(3)
        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "verification_valid",
                "$expiredTime 까지 유효한 인증이 있습니다."
            )
        )

        verificationService.stub {
            on { generateJoinVerification(any()) } doThrow AlreadyVerifiedException(expiredTime)
        }

        mockMvc.perform(
            post("/v1/verification/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun wrongTypePhoneNumber() {
        val wrongPhoneNumberShape = "010-8976-1234"
        val request = VerificationRequest(wrongPhoneNumberShape)

        val content = objectMapper.writeValueAsString(request)

        doNothing().`when`(verificationService).generateJoinVerification(any())

        mockMvc.perform(
            post("/v1/verification/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun generatingVerificationForResetSuccess() {
        val phoneNumber = "01089761234"
        val request = VerificationRequest(phoneNumber)

        val content = objectMapper.writeValueAsString(request)

        doNothing().`when`(verificationService).generateResetVerification(any())

        mockMvc.perform(
            post("/v1/verification/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isCreated)
    }

    @Test
    fun notMemberTryResetVerification() {
        val phoneNumber = "01089761234"
        val request = VerificationRequest(phoneNumber)

        val content = objectMapper.writeValueAsString(request)
        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "not_found_member",
                "없는 회원입니다."
            )
        )

        verificationService.stub {
            on { generateResetVerification(any()) } doThrow NoMemberFoundException()
        }

        mockMvc.perform(
            post("/v1/verification/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isNotFound)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun ongoingResetVerification() {
        val phoneNumber = "01089761234"
        val request = VerificationRequest(phoneNumber)

        val content = objectMapper.writeValueAsString(request)
        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "verification_ongoing",
                "진행중인 인증이 있습니다."
            )
        )

        verificationService.stub {
            on { generateResetVerification(any()) } doThrow OngoingVerificationException()
        }

        mockMvc.perform(
            post("/v1/verification/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )

            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun alreadyVerifiedResetVerification() {
        val phoneNumber = "01089761234"
        val request = VerificationRequest(phoneNumber)

        val content = objectMapper.writeValueAsString(request)
        val expiredTime = LocalDateTime.now().plusMinutes(3)
        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "verification_valid",
                "$expiredTime 까지 유효한 인증이 있습니다."
            )
        )

        verificationService.stub {
            on { generateResetVerification(any()) } doThrow AlreadyVerifiedException(expiredTime)
        }

        mockMvc.perform(
            post("/v1/verification/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )

            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun confirmingVerificationSuccess() {
        val phoneNumber = "01089761234"
        val code = "123456"
        val request = VerificationConfirmRequest(
            phoneNumber,
            code,
            "JOIN"
        )

        val content = objectMapper.writeValueAsString(request)

        doNothing().`when`(verificationService).confirmVerification(any())

        mockMvc.perform(
            patch("/v1/verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isOk)
    }

    @Test
    fun notFoundVerification() {
        val phoneNumber = "01089761234"
        val code = "123456"
        val request = VerificationConfirmRequest(
            phoneNumber,
            code,
            "JOIN"
        )

        val content = objectMapper.writeValueAsString(request)
        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "verification_not_found",
                "찾을 수 없는 인증입니다."
            )
        )

        verificationService.stub {
            on { confirmVerification(any()) } doThrow NotExistingVerificationException()
        }

        mockMvc.perform(
            patch("/v1/verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isNotFound)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun codeNotMatchedVerification() {
        val phoneNumber = "01089761234"
        val code = "123456"
        val request = VerificationConfirmRequest(
            phoneNumber,
            code,
            "JOIN"
        )

        val content = objectMapper.writeValueAsString(request)
        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "code_not_matched",
                "코드가 매치하지 않습니다."
            )
        )

        verificationService.stub {
            on { confirmVerification(any()) } doThrow CodeNotMatchedException()
        }

        mockMvc.perform(
            patch("/v1/verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )

            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }

    @Test
    fun codeExpiredVerification() {
        val phoneNumber = "01089761234"
        val code = "123456"
        val request = VerificationConfirmRequest(
            phoneNumber,
            code,
            "JOIN"
        )

        val content = objectMapper.writeValueAsString(request)
        val expectedErrorResponse = objectMapper.writeValueAsString(
            ErrorResponse(
                "code_expired",
                "코드 입력 시간이 지났습니다."
            )
        )

        verificationService.stub {
            on { confirmVerification(any()) } doThrow CodeExpiredException()
        }

        mockMvc.perform(
            patch("/v1/verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
            .andExpect(content().json(expectedErrorResponse))
    }
}
