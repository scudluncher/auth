package com.ragnarok.auth.common.response

sealed class Meta(
    private val result: String,
    private val resultMsg: String,
    private val errorCode: String,
) {
    private val code = 0L

    class Ok : Meta("ok", "", "")
    class Fail(code: String, message: String) : Meta("fail", message, code)
}
