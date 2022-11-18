package com.ragnarok.auth.common.extension

import com.ragnarok.auth.common.exception.AuthorizationException
import com.ragnarok.auth.common.support.TokenPayload
import org.springframework.security.core.context.SecurityContextHolder

interface AuthenticationExtension {
    fun tokenPayload(): TokenPayload? {
        return try {
            SecurityContextHolder.getContext()
                ?.authentication
                ?.let { it.principal as TokenPayload }
        } catch (e: ClassCastException) {
            null
        }
    }

    fun nickName(): String {
        return tokenPayload()?.nickName ?: throw AuthorizationException()
    }

    fun id(): Long {
        return tokenPayload()?.id ?: throw AuthorizationException()
    }
}
