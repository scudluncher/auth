package com.ragnarok.auth.auth.controller

import com.ragnarok.auth.auth.exception.AuthenticationFailException
import com.ragnarok.auth.auth.request.LoginRequest
import com.ragnarok.auth.auth.service.AuthService
import com.ragnarok.auth.common.exception.BadRequestException
import com.ragnarok.auth.common.infra.controller.ControllerTestExtension
import com.ragnarok.auth.common.response.SingleResponse
import com.ragnarok.auth.common.support.TokenProvider
import com.ragnarok.auth.member.domain.entity.Member
import com.ragnarok.auth.member.domain.value.Email
import com.ragnarok.auth.member.domain.value.HashedPassword
import com.ragnarok.auth.member.exception.NoMemberFoundException
import com.ragnarok.auth.member.viewmodel.AuthResult
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AuthController::class)
class AuthControllerTest : ControllerTestExtension, AnnotationSpec() {
    @InjectMocks
    private lateinit var authController: AuthController

    @Mock
    private lateinit var authService: AuthService

    @Mock
    private lateinit var tokenProvider: TokenProvider

    private lateinit var mockMvc: MockMvc

    private val objectMapper = objectMapper()

    override suspend fun beforeSpec(spec: Spec) {
        MockitoAnnotations.openMocks(this)
        mockMvc = defaultMockMvcBuilder(authController)
            .build()
    }

    @Test
    fun loginSuccess() {
        val phoneNumber = "01089764629"
        val password = "123456asdf"
        val request = LoginRequest(
            phoneNumber = phoneNumber,
            password = password,
            id = null,
            nickName = null,
            email = null
        )

        val content = objectMapper.writeValueAsString(request)

        val member = Member(
            333L,
            Email("scudluncher@gmail.com"),
            HashedPassword("asdfqwer", "zxcv"),
            "nameless one",
            "also no nick name",
            "01089764629"
        )
        val token = "some.verylong.token"
        val expectedResponse = objectMapper.writeValueAsString(
            SingleResponse.Ok(
                AuthResult(token)
            )
        )

        authService.stub {
            on { login(any()) } doReturn member
        }

        tokenProvider.stub {
            on { token(any()) } doReturn token
        }

        mockMvc.perform(
            post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(content().json(expectedResponse))
    }

    @Test
    fun loginFailByNoSuchMember() {
        val phoneNumber = "01089764629"
        val password = "123456asdf"
        val request = LoginRequest(
            phoneNumber = phoneNumber,
            password = password,
            id = null,
            nickName = null,
            email = null
        )

        val content = objectMapper.writeValueAsString(request)

        authService.stub {
            on { login(any()) } doThrow NoMemberFoundException::class
        }

        mockMvc.perform(
            post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun loginFailByMultipleIdentification() {
        val phoneNumber = "01089764629"
        val password = "123456asdf"
        val email = "wrong@mail.com"
        val request = LoginRequest(
            phoneNumber = phoneNumber,
            password = password,
            id = null,
            nickName = null,
            email = email
        )

        val content = objectMapper.writeValueAsString(request)

        authService.stub {
            on { login(any()) } doThrow BadRequestException::class
        }

        mockMvc.perform(
            post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isConflict)
    }

    @Test
    fun loginFailByWrongPasswordOrWrongIdentification() {
        val phoneNumber = "01089764629"
        val password = "123456asdf"
        val request = LoginRequest(
            phoneNumber = phoneNumber,
            password = password,
            id = null,
            nickName = null,
            email = null
        )

        val content = objectMapper.writeValueAsString(request)

        authService.stub {
            on { login(any()) } doThrow AuthenticationFailException::class
        }

        mockMvc.perform(
            post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        )
            .andExpect(status().isUnauthorized)
    }
}

