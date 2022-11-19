package com.ragnarok.auth.verification.usecase

import com.ragnarok.auth.common.support.ExternalMessageSender
import com.ragnarok.auth.common.value.SmsMessage
import com.ragnarok.auth.member.domain.repository.MemberRepository
import com.ragnarok.auth.member.exception.AlreadyRegisteredMemberException
import com.ragnarok.auth.member.exception.NoMemberFoundException
import com.ragnarok.auth.verification.domain.entity.Verification
import com.ragnarok.auth.verification.domain.repository.VerificationRepository
import com.ragnarok.auth.verification.domain.value.VerificationType
import com.ragnarok.auth.verification.value.TimeLimit
import java.time.LocalDateTime
import kotlin.random.Random

class GeneratingJoinVerification(
    private val request: VerificationRequest,
    private val verificationRepository: VerificationRepository,
    private val memberRepository: MemberRepository,
    private val messageSender: ExternalMessageSender,
) {
    fun execute() {
        memberRepository.findByPhoneNumber(request.phoneNumber)
            ?.let { throw AlreadyRegisteredMemberException() }

        verificationRepository.findByPhoneNumberAndType(request.phoneNumber, VerificationType.JOIN)
            ?.checkOngoingStatus()

        val verification = Verification(
            null,
            request.phoneNumber,
            randomCode(),
            LocalDateTime.now().plusMinutes(TimeLimit.CODE_EXPIRED),
            VerificationType.JOIN
        )

        verificationRepository.save(verification)

        messageSender.sendMessage(SmsMessage(verification.phoneNumber, verification.code))
    }
}

class GeneratingResetVerification(
    private val request: VerificationRequest,
    private val verificationRepository: VerificationRepository,
    private val memberRepository: MemberRepository,
    private val messageSender: ExternalMessageSender,
) {
    fun execute() {
        memberRepository.findByPhoneNumber(request.phoneNumber)
            ?: throw NoMemberFoundException()

        verificationRepository.findByPhoneNumberAndType(request.phoneNumber, VerificationType.RESET)
            ?.checkOngoingStatus()

        val verification = Verification(
            null,
            request.phoneNumber,
            randomCode(),
            LocalDateTime.now().plusMinutes(TimeLimit.CODE_EXPIRED),
            VerificationType.RESET
        )

        verificationRepository.save(verification)

        messageSender.sendMessage(
            SmsMessage(verification.phoneNumber, verification.code)
        )
    }
}

class VerificationRequest(val phoneNumber: String)

fun randomCode(): String {
    return List(6) { Random.nextInt(0, 9) }
        .joinToString(separator = "")
}



