package com.ragnarok.auth.common.support

import com.ragnarok.auth.common.exception.InitializingFailException
import java.security.*
import java.security.spec.ECGenParameterSpec


interface KeyStorage {
    val privateKey: PrivateKey
    val publicKey: PublicKey

    fun keyPair(): KeyPair {
        return KeyPair(publicKey, privateKey)
    }
}


//interface JwsKeyStorage : KeyStorage {
//
//}
