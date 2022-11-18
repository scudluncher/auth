package com.ragnarok.auth.common.support

interface HashingProvider {
    fun salt(): String
    fun hash(password: String, salt: String): String
}
