package com.ragnarok.auth.common.support

interface TokenProvider {
    fun token(payload: TokenPayload): String
    fun payload(token: String): TokenPayload
}

class TokenPayload(
    val id: Long,
)
