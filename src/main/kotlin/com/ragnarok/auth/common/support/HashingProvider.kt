package com.ragnarok.auth.common.support

import kotlin.random.Random

interface HashingProvider {
    fun salt(): String
    fun hash(password: String, salt: String): String
}

class FakeHashingProvider : HashingProvider {
    override fun salt(): String {
        return "salt"
    }

    override fun hash(password: String, salt: String): String {
        return salt + password
    }
}
