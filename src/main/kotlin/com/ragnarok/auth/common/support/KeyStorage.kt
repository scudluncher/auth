package com.ragnarok.auth.common.support

import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey

interface KeyStorage {
    val privateKey: PrivateKey
    val publicKey: PublicKey

    fun keyPair(): KeyPair {
        return KeyPair(publicKey, privateKey)
    }
}
