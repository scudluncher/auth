package com.ragnarok.auth.common.infra.controller

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.ragnarok.auth.auth.advice.AuthErrorHandlingAdvice
import com.ragnarok.auth.common.advice.CommonErrorHandlingAdvice
import com.ragnarok.auth.common.support.TokenAuthentication
import com.ragnarok.auth.common.support.TokenPayload
import com.ragnarok.auth.member.advice.MemberErrorHandlingAdvice
import com.ragnarok.auth.verification.advice.VerificationErrorHandlingAdvice
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.filter.CharacterEncodingFilter

interface ControllerTestExtension {
    fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .registerModules(
                ParameterNamesModule()
            )
    }

    fun defaultMockMvcBuilder(
        controller: Any,
    ): StandaloneMockMvcBuilder {
        return MockMvcBuilders.standaloneSetup(controller)
            .addFilters<StandaloneMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<StandaloneMockMvcBuilder>(MockMvcResultHandlers.print())
            .setControllerAdvice(
                AuthErrorHandlingAdvice(),
                MemberErrorHandlingAdvice(),
                VerificationErrorHandlingAdvice(),
                CommonErrorHandlingAdvice(),
            )
            .setMessageConverters(httpMessageConverter<Any>())
    }

    fun <T> httpMessageConverter(): MappingJackson2HttpMessageConverter {
        val objectMapper = objectMapper()
        return MappingJackson2HttpMessageConverter(objectMapper)
    }

    //make it as if user login is done
    fun setSecurityContext() {
        SecurityContextHolder.clearContext()

        SecurityContextHolder.getContext()
            .authentication = TokenAuthentication(
            TokenPayload(
                130L,
                "K810 HUNTER"
            )
        )
    }
}
