package com.ragnarok.auth.common.support

import com.ragnarok.auth.common.value.Message

interface ExternalMessageSender {
    fun sendMessage(message: Message)
}
