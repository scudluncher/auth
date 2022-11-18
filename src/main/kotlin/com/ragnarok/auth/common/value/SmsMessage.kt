package com.ragnarok.auth.common.value

class SmsMessage(
    val receiverPhoneNumber: String,
    val contents: String,
) : Message {
    override fun receiver(): String {
        return this.receiverPhoneNumber
    }

    override fun contents(): String {
        return this.contents
    }
}
