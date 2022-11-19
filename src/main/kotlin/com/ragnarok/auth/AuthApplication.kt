package com.ragnarok.auth

import com.ragnarok.auth.common.support.TokenInterceptor
import com.ragnarok.auth.common.support.TokenProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class AuthApplication {

    @Configuration
    class Config(
        private val tokenProvider: TokenProvider,
    ) : WebMvcConfigurer {
        override fun addInterceptors(registry: InterceptorRegistry) {
            registry.addWebRequestInterceptor(
                TokenInterceptor(tokenProvider)
            )
                .addPathPatterns("/v1/**")
                .excludePathPatterns(
                    "/v1/members",
                    "/v1/members/password-reset",
                    "/v1/verification/*",
                    "/v1/verification",
                    "/v1/auth/login"
                )
        }
    }
}

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig {
    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity.httpBasic().disable()
        httpSecurity.headers().frameOptions().disable() // h2 console 접속 용도
        httpSecurity.csrf().disable()
        return httpSecurity.build()
    }
}


fun main(args: Array<String>) {
    runApplication<AuthApplication>(*args)
}
