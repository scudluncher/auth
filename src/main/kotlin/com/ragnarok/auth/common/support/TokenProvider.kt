package com.ragnarok.auth.common.support

interface TokenProvider {
    fun token(payload: TokenPayload): String
    fun payload(token: String): TokenPayload
}

class TokenPayload(
    val id: Long,
    val nickName: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TokenPayload

        if (id != other.id) return false
        if (nickName != other.nickName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + nickName.hashCode()
        return result
    }
}
