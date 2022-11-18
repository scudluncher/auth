package com.ragnarok.auth.member.domain.value

class HashedPassword(
    val hashed: String,
    val salt: String,
) {
    fun checkEquality(hashed: String): Boolean {
        return this.hashed == hashed
    }
}
