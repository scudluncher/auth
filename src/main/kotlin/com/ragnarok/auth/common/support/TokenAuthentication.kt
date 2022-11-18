package com.ragnarok.auth.common.support

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class TokenAuthentication(private val payload: TokenPayload) : Authentication {
    override fun getAuthorities(): Collection<GrantedAuthority>? {
        return null
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getDetails(): Any? {
        return null
    }

    override fun getPrincipal(): Any {
        return payload
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
    }

    override fun getName(): String {
        return payload.nickName
    }
}
