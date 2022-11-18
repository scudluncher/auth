package com.ragnarok.auth.common.infra

import com.ragnarok.auth.common.exception.HashingException
import com.ragnarok.auth.common.support.HashingProvider
import org.springframework.stereotype.Component
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@Component
class PBKDF2HashingProvider : HashingProvider {
    override fun salt(): String {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    override fun hash(password: String, salt: String): String {
        val spec = PBEKeySpec(
            password.toCharArray(),
            Base64.getDecoder().decode(salt),
            65536,
            128
        )

        return try {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            Base64.getEncoder().encodeToString(
                factory.generateSecret(spec).encoded
            )
        } catch (e: GeneralSecurityException) {
            throw HashingException(e)
        }
    }
}
