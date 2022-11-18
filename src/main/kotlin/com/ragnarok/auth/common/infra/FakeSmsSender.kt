package com.ragnarok.auth.common.infra

import com.ragnarok.auth.common.support.ExternalMessageSender
import com.ragnarok.auth.common.value.Message
import org.springframework.stereotype.Component

@Component
class FakeSmsSender : ExternalMessageSender {
    override fun sendMessage(message: Message) {
        //console 에 찍고 메시지를 보낸다고 가정합니다.
        println("receiver: ${message.receiver()} // contents: ${message.contents()}"       )
    }
}
