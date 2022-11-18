package com.ragnarok.auth.common.support

import com.ragnarok.auth.common.exception.AuthorizationException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.ui.ModelMap
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.WebRequestInterceptor

class TokenInterceptor(private val tokenProvider: TokenProvider) : WebRequestInterceptor {
    override fun preHandle(request: WebRequest) {
        SecurityContextHolder.clearContext()

        val authorization = request.getHeader("Authorization")
        val token = with(authorization) {
            if (this == null || !this.startsWith("Bearer ")) {
                throw AuthorizationException()
            }
            this.substring(7)
        }

        try {
            val payload = tokenProvider.payload(token)
            SecurityContextHolder.getContext().authentication = TokenAuthentication(payload)
        } catch (e: ExpiredJwtException) {
            throw e
        } catch (e: JwtException) {
            throw AuthorizationException(e)
        }
    }

    override fun postHandle(request: WebRequest, model: ModelMap?) {}

    override fun afterCompletion(request: WebRequest, ex: Exception?) {}
}
