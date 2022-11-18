package com.ragnarok.auth.common.response

sealed class SingleResponse<C>(private val content: C, private val meta: Meta) {
    class Ok<T>(content: T) : SingleResponse<T>(content, Meta.Ok())

    class Fail(
        code: String,
        message: String,
    ) : SingleResponse<EmptyContent>(
        EmptyContent(), Meta.Fail(code, message)
    )
}
