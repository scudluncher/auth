package com.ragnarok.auth.common.value

interface Message {
    fun receiver(): String
    fun contents(): String
}
