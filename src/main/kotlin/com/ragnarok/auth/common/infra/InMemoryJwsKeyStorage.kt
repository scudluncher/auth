package com.ragnarok.auth.common.infra

import com.ragnarok.auth.common.exception.InitializingFailException
import com.ragnarok.auth.common.support.KeyStorage
import org.springframework.stereotype.Component
import java.security.*
import java.security.spec.ECGenParameterSpec

@Component
class InMemoryJwsKeyStorage : KeyStorage {
    final override val privateKey: PrivateKey
    final override val publicKey: PublicKey

    init {
        val keyPair = generateKeyPair()
        privateKey = keyPair.private
        publicKey = keyPair.public
    }

    companion object {
        fun generateKeyPair(): KeyPair {
            return try {
                val keyGenerator = KeyPairGenerator.getInstance("EC")
                keyGenerator.initialize(ECGenParameterSpec("secp256r1"))
                keyGenerator.generateKeyPair()
            } catch (e: GeneralSecurityException) {
                throw InitializingFailException(e)
            }
        }
    }
}
