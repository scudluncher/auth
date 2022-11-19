package com.ragnarok.auth.common.infra

import com.ragnarok.auth.common.support.KeyStorage
import com.ragnarok.auth.common.support.TokenPayload
import com.ragnarok.auth.common.support.TokenProvider
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*

@Component
class JwtTokenProvider(
    private val keyStorage: KeyStorage,
) : TokenProvider {
    override fun token(payload: TokenPayload): String {
        return Jwts.builder()
            .signWith(keyStorage.privateKey)
            .claim("id", payload.id)
            .claim("nickName", payload.nickName)
            .setExpiration(defaultExpiresIn())
            .compact()
    }

    override fun payload(token: String): TokenPayload {
        val claims: Claims = Jwts.parserBuilder()
            .setSigningKey(keyStorage.publicKey)
            .build()
            .parseClaimsJws(token)
            .body

        return TokenPayload(
            claims.get("id", Integer::class.java).toLong(),
            claims.get("nickName", String::class.java),
        )
    }

    private fun defaultExpiresIn(): Date {
        return Date.from(
            ZonedDateTime.now()
                .plusMinutes(20)
                .toInstant()
        )
    }
}
